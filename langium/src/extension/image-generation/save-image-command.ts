import * as vscode from 'vscode';
import { Format } from './image-generator.js';

//creates the command required to run to generate the image
export class SaveImageCommand{
	private jar_file: vscode.Uri; //location of the jar file
	private log_level: string; //log level setting

    private editor!: vscode.TextEditor; //current editor
    private document!: vscode.TextDocument; //current document
    private directory!: vscode.WorkspaceFolder; //current directory

	constructor(context: vscode.ExtensionContext, editor: vscode.TextEditor | undefined){
        //the extension context should not change
        this.jar_file = vscode.Uri.joinPath(context.extensionUri, 'jar', 'jpipe.jar');
        
        //the log level should only change on configuration change
        this.log_level = "all";

        //automatically registers to start
        this.update(editor);
	}

    //updates each time the editor changes
    public async update(editor: vscode.TextEditor | undefined): Promise<void>{
        if(!editor){
            return;
        }

        this.editor = editor;
        this.document = editor.document;
        
        let directory = vscode.workspace.getWorkspaceFolder(this.document.uri);
        if(directory){
            this.directory = directory;
        }
    }
	
	//creates the command based on command settings
	public async makeCommand(command_settings: CommandSettings): Promise<string>{
		let input_file = this.document.uri;
		let diagram_name = this.findDiagramName(this.document,this.editor);
		let format = this.getFormat(command_settings);
		
		let command = 'java -jar ' + this.jar_file.path + ' -i ' + input_file.path + ' -d '+ diagram_name + ' --format ' + format + ' --log-level ' + this.log_level;
		
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

		let default_output_file =vscode.Uri.joinPath(this.directory.uri, diagram_name + "." + command_settings.format.toLowerCase());

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
}

type CommandSettings = {
	format?: Format,
	save_image: boolean
}
