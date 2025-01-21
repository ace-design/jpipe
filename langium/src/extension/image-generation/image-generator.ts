import * as vscode from 'vscode';
import util from "node:util";
import { ConfigKey } from '../configuration/index.js';
import { OutputManager, ConfigurationManager, EventSubscriber, isTextEditor, Command, CommandUser, JPipeOutput } from '../managers/index.js';

export class ImageGenerator implements CommandUser, EventSubscriber<vscode.TextEditor | undefined>{

	//configuration manager to fetch configurations from
	private configuration: ConfigurationManager;

    private editor!: vscode.TextEditor; //current editor
    private document!: vscode.TextDocument; //current document
    private directory!: vscode.WorkspaceFolder; //current directory

	//possible image types and associated commands
	private types: ImageType[]; 

	private current_jar_file: string ;
    
	constructor(configuration: ConfigurationManager, private readonly output_manager: OutputManager) {
		this.configuration = configuration;
		this.current_jar_file = "";
		
		this.types = [
			{
				exe_command: "jpipe.downloadPNG", 
				format: Format.PNG
			},
			{
				exe_command: "jpipe.downloadSVG", 
				format: Format.SVG
			}
		];
		
        //automatically registers to start
        this.update(vscode.window.activeTextEditor);
	}

	//used to register commands with command manager
	public getCommands(): Command[] | Command{
		let command_list: Command[] = [];
		this.types.forEach( (type) =>{
			command_list.push({
				command: type.exe_command,
				callback: () => {this.generate({format: type.format, save_image: true})}
			});
		});

		return command_list;
	}

    //updater functions
    public async update(data: vscode.TextEditor | undefined): Promise<void>{
		if(isTextEditor(data)){
			const {editor, document, directory} = this.updateEditor(data);
			
			this.editor = editor;
			this.document = document;
			this.directory = directory;
		}
    }

	public async generate(command_settings: CommandSettings): Promise<{stdout: any}>{
		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);

		const command = await this.makeCommand(command_settings);
		const output: {error: any, stdout: any, stderr: any} = await execPromise(command);

		return {stdout: output.stdout};
	}

	//creates the command based on command settings
	private async makeCommand(command_settings: CommandSettings): Promise<string>{
		let jar_file = this.configuration.getConfiguration(ConfigKey.JARFILE);
		
		let input_file = this.document.uri;
		
		let diagram_name = this.findDiagramName(this.document,this.editor);
		
		let format = this.getFormat(command_settings);
		
		let log_level = this.configuration.getConfiguration(ConfigKey.LOGLEVEL);
		

		let command = 'java -jar ' + jar_file + ' -i ' + input_file.path + ' -d '+ diagram_name + ' --format ' + format + ' --log-level ' + log_level;
		

		this.output_manager.log(JPipeOutput.USER, this.generateUserMessage(jar_file));

		this.output_manager.log(JPipeOutput.CONSOLE, "Image made using jar file: " + jar_file.toString()); //Shows user relevant info


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

	//helper function to perform updates related to a new text editor
	private updateEditor(editor: vscode.TextEditor | undefined): {editor: vscode.TextEditor, document: vscode.TextDocument, directory: vscode.WorkspaceFolder}{
		let document: vscode.TextDocument;
		let directory: vscode.WorkspaceFolder;

		if(!editor){
			editor = this.editor;
			document = this.document;
			directory = this.directory;
		}else{
			document = editor.document;
			directory = this.getDirectory(document);
		}

		return{editor, document, directory};
	}

	//helper function to get directory for updating
	private getDirectory(document: vscode.TextDocument): vscode.WorkspaceFolder{
		let directory = vscode.workspace.getWorkspaceFolder(document.uri);

		if(!directory){
			directory = this.directory;
		}

		return directory;
	}
		
	//helper function to generate the user message upon image generation
	private generateUserMessage(jar_file: string): string{
		let user_message: string = "";
		
		if(jar_file !== ""){
			user_message += "\nImage Changed! Generating new image "
			
			if(jar_file !== this.current_jar_file){
				user_message += "using jar file: " + jar_file;
			}else{
				user_message += "using previous jar file"
			}
		}else{
			user_message += "Using jar file: " + jar_file;
		}

		this.current_jar_file = jar_file;
		
		return user_message;
	}
}

type ImageType = {
	exe_command: string,
	format: Format
}

export enum Format{
	PNG = "PNG",
	SVG = "SVG",
}

type CommandSettings = {
	format?: Format,
	save_image: boolean
}