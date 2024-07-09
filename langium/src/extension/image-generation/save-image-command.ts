import * as vscode from 'vscode';
import { Format } from './image-generator.js';
import { EventSubscriber, isConfigurationChangeEvent, isTextEditor } from '../managers/event-manager.js';

//creates the command required to run to generate the image
export class SaveImageCommand implements EventSubscriber<vscode.TextEditor | undefined>, EventSubscriber<vscode.ConfigurationChangeEvent>{
	private output_channel: vscode.OutputChannel
	
	private jar_file: string; //location of the jar file
	private log_level: string; //log level setting

    private editor!: vscode.TextEditor; //current editor
    private document!: vscode.TextDocument; //current document
    private directory!: vscode.WorkspaceFolder; //current directory

	constructor(private readonly context: vscode.ExtensionContext, editor: vscode.TextEditor | undefined, output_channel: vscode.OutputChannel){
        this.output_channel = output_channel;
		
		//Finding the associated compiler
        this.jar_file = this.getJarFile();

        //the log level should only change on configuration change
		this.log_level = this.getLogLevel();

        //automatically registers to start
        this.update(editor);
	}

    //updater functions
	public async update(change: vscode.ConfigurationChangeEvent): Promise<void>;
    public async update(editor: vscode.TextEditor | undefined): Promise<void>;
	public async update(data: vscode.ConfigurationChangeEvent | vscode.TextEditor | undefined): Promise<void>{
        if(isConfigurationChangeEvent(data)){
			if(data.affectsConfiguration("jpipe.logLevel")){
				this.log_level = this.getLogLevel();
			}
			else if(data.affectsConfiguration("jpipe.jarFile")){
				this.jar_file = this.getJarFile();
			}
		}else if(isTextEditor(data)){
			this.editor = this.getEditor(data);
			
			this.document = this.editor.document;
			
			this.directory = this.getDirectory(this.document);
		}
    }
	
	//creates the command based on command settings
	public async makeCommand(command_settings: CommandSettings): Promise<string>{
		let input_file = this.document.uri;
		let diagram_name = this.findDiagramName(this.document,this.editor);
		let format = this.getFormat(command_settings);
		
		let command = 'java -jar ' + this.jar_file + ' -i ' + input_file.path + ' -d '+ diagram_name + ' --format ' + format + ' --log-level ' + this.log_level;
		
		if(command_settings.save_image){
			let output_file = await this.makeOutputPath(diagram_name, command_settings);
			command += ' -o ' + output_file.path;
		}
		
		return command;
	}

	//returns the current diagram name
	public getDiagramName(): string{
		return this.findDiagramName(this.document, this.editor);
	}

	//helper function to get the diagram name from the document
	private findDiagramName(document: vscode.TextDocument, editor: vscode.TextEditor): string{
		let diagram_name: string | undefined;
		let match: RegExpExecArray | null;
		let i = 0;
	
		let lines = document.getText().split("\n");
		let line_num = editor.selection.active.line + 1;

		while(i < lines.length && (i < line_num || diagram_name===null)){
			match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
		
			if (match){
				diagram_name = match[0].split(' ')[1];
			}

			i++;
		}

		if(diagram_name === undefined){
			throw new Error("Diagram name not found");
		}
		
		return diagram_name;
	}

	//helper function to set default format
	private getFormat(command_settings: CommandSettings): Format{
		let format = command_settings.format;
		
		if(format === undefined){
			format = Format.PNG;
		}
		
		return format;
	}

	//creates the output filepath by asking for user input
    private async makeOutputPath(diagram_name: string, command_settings: CommandSettings): Promise<vscode.Uri>{
		if(command_settings.format === undefined){
			command_settings.format = Format.PNG;
		}

		let default_output_file = vscode.Uri.joinPath(this.directory.uri, diagram_name + "." + command_settings.format.toLowerCase());

		let save_dialog_options: vscode.SaveDialogOptions = {
			defaultUri:  default_output_file,
			saveLabel: "Save proof model",
			filters: {
					'Images': [Format.PNG.toLowerCase(), Format.SVG.toLowerCase()]
				}
		}

        let output_file = await vscode.window.showSaveDialog(save_dialog_options);
        
        if(!output_file){
			throw new Error("Please enter a save location");
		}
        
        return output_file;
    }

	//helper function to get editor for updating
	private getEditor(editor: vscode.TextEditor | undefined): vscode.TextEditor{
		if(!editor){
			editor = this.editor;
		}
		
		return editor;
	}

	//helper function to get directory for updating
	private getDirectory(document: vscode.TextDocument): vscode.WorkspaceFolder{
		let directory = vscode.workspace.getWorkspaceFolder(document.uri);

		if(!directory){
			throw new Error("Directory not found");
		}

		return directory;
	}

	//helper function to fetch the log level on configuration change
	private getLogLevel(): string{
		let log_level: string;
		let configuration = vscode.workspace.getConfiguration().inspect("jpipe.logLevel")?.globalValue;
			
		if(typeof configuration === "string"){
			log_level = configuration;
		}else{
			log_level = "error";
		}

		return log_level;
	}

	//helper function to fetch the input jar file path
	private getJarFile(): string{
		let jar_file: string;
		let default_value = "";//must be kept in sync with the actual default value manually
		let configuration = vscode.workspace.getConfiguration().inspect("jpipe.jarFile")?.globalValue;
		
		if(typeof configuration === "string"){
			jar_file = configuration;
		}else{
			jar_file = default_value;
		}
		
		if(jar_file === default_value){
			jar_file = vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').path;
			vscode.workspace.getConfiguration().update("jpipe.jarFile", jar_file);
		}else if(!this.jarPathExists(jar_file)){
			throw new Error("Specified jar path does not exist");
		}

		return jar_file;
	}

	private jarPathExists(file_path: string): boolean{
		let jar_path_exists: boolean;
		
		try{
			let file = vscode.Uri.file(file_path);
			let new_uri =  vscode.Uri.joinPath(this.directory.uri, "example.jar");
			
			vscode.workspace.fs.copy(file, new_uri);
			vscode.workspace.fs.delete(new_uri);
			
			jar_path_exists = true;
		}catch(error: any){
			this.output_channel.appendLine(error.toString());

			jar_path_exists = false;
		}
		return jar_path_exists;
	}
}

type CommandSettings = {
	format?: Format,
	save_image: boolean
}
