import * as vscode from 'vscode';
import util from "node:util";
import { Disposable } from 'vscode-languageserver';

export abstract class ImageGenerator{

	// Defines the command needed to execute the extension. 
	protected abstract exe_command: string;

	//defines the file type being used
	protected abstract format: Format;
	
	// New channel created in vscode terminal for user debugging.
	private static output_channel = vscode.window.createOutputChannel("jpipe_image");

    constructor( private readonly context: vscode.ExtensionContext) {
		
	}

	public register(): Disposable{
		vscode.commands.registerCommand(this.exe_command, () => {
			this.createImage();
		});
		return {
			dispose: function (){}
		};
	}


	//Generates an image for the selected justification diagram
	public async createImage(): Promise<void> {
		let document = vscode.window.activeTextEditor?.document;
		if(!document){
			throw new Error("Text Editor not found");
		}

		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);
        
		const directory = vscode.workspace.getWorkspaceFolder(document.uri)?.uri;
		if(!directory){
			throw new Error("Current Directory not found");;
		}

		// Store the path to the jar executable file.
		let jar_file = vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar');

		// Store the path to the jd file that needs to be compiled.
		let input_file = document.uri;
		
		let log_level = "all";
		let diagram_name = this.getDiagramName(document);
		
		let default_output_file = vscode.Uri.joinPath(directory, diagram_name + "." + this.format.toLowerCase());

		// Execute the command, and wait for the result (must be synchronous).
		// TODO: Validate that this actually executes synchronously. 
		try{
			let save_dialog_options: vscode.SaveDialogOptions = {
				defaultUri:  default_output_file,
				saveLabel: "Save proof model",
				filters: {
					    'Images': ['png', 'svg']
					}
			}
			let output_file: vscode.Uri | undefined = await vscode.window.showSaveDialog(save_dialog_options);
			
			if(!output_file){
				throw new Error("Please enter a save location");
			}

			let command  = 'java -jar ' + jar_file.path + ' -i ' + input_file.path + ' -d '+ diagram_name + ' --format ' + this.format + ' --log-level ' + log_level + ' -o ' + output_file.path
			const {stdout, stderr} = await execPromise(command);

			ImageGenerator.output_channel.appendLine(stdout.toString());
			ImageGenerator.output_channel.appendLine(stderr.toString());
		} catch (error: any){
			ImageGenerator.output_channel.appendLine(error.toString());
		}
	}

	//helper function to get the line number of the cursor position
	private getLineNumber(): number{
		const editor = vscode.window.activeTextEditor;
		if(editor){
			let cursor_pos = editor.selection.active;
			return cursor_pos.line + 1;
		}else{
			throw new Error("Editor not found");
		}
	}
	
	//helper function to get the diagram name from the document
	private getDiagramName(document: vscode.TextDocument): string{
		let diagram_name: string | undefined;
		let match: RegExpExecArray | null;
		let i = 0;
		
		let lines = document.getText().split("\n");
		let line_num = this.getLineNumber();

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

}

export enum Format{
	PNG = "PNG",
	SVG = "SVG"
}

// export class SaveImageCommand{
// 	private default_output_file: vscode.Uri;
// 	private jar_file: vscode.Uri;
// 	private input_file: vscode.Uri;

// 	private diagram_name: string;
// 	private log_level: string;

// 	private format: Format;

// 	constructor(editor: vscode.TextEditor, context: vscode.ExtensionContext, format: Format){
// 		let document = editor.document;
		
// 		const directory = vscode.workspace.getWorkspaceFolder(document.uri)?.uri;
// 		if(!directory){
// 			throw new Error("Current Directory not found");;
// 		}

// 		// Store the path to the jar executable file.
// 		this.jar_file = vscode.Uri.joinPath(context.extensionUri, 'jar', 'jpipe.jar');

// 		// Store the path to the jd file that needs to be compiled.
// 		this.input_file = document.uri;
	
// 		this.log_level = "all";
// 		this.format = format; 
// 		this.diagram_name = this.getDiagramName(document);
// 		this.default_output_file = this.makeOutputPath(directory, this.diagram_name);
// 	}

// 	public async makeCommand(): Promise<string>{
// 		let save_dialog_options: vscode.SaveDialogOptions = {
// 			defaultUri:  this.default_output_file,
// 			saveLabel: "Save proof model",
// 			filters: {
// 					'Images': ['png', 'svg']
// 				}
// 		}

// 		let output_file: vscode.Uri | undefined = await vscode.window.showSaveDialog(save_dialog_options);
		
// 		if(!output_file){
// 			throw new Error("Please enter a save location");
// 		}

// 		return 'java -jar ' + this.jar_file.path + ' -i ' + this.input_file.path + ' -d '+ this.diagram_name + ' --format ' + this.format + ' --log-level ' + this.log_level + ' -o ' + output_file.path
// 	}

// 	//helper function to get the line number of the cursor position
//     private getLineNumber(): number{
//         const editor = vscode.window.activeTextEditor;
//         if(editor){
//             let cursor_pos = editor.selection.active;
//             return cursor_pos.line + 1;
//         }else{
//             throw new Error("Editor not found");
//         }
//     }

// 	//helper function to get the diagram name from the document
// 	private getDiagramName(document: vscode.TextDocument): string{
// 		let diagram_name: string | undefined;
// 		let match: RegExpExecArray | null;
// 		let i = 0;
	
// 		let lines = document.getText().split("\n");
// 		let line_num = this.getLineNumber();

// 		while(i < lines.length && (i < line_num || diagram_name===null)){
// 			match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
		
// 			if (match){
// 				diagram_name = match[0].split(' ')[1];
// 			}

// 			i++;
// 		}

// 		if(diagram_name === undefined){
// 			throw new Error("Diagram name not found");
// 		}

// 		return diagram_name;
// 	}

// 	//helper function to create the output path
// 	private makeOutputPath(directory: vscode.Uri, diagram_name: string): vscode.Uri {
// 		let temp_dir = vscode.Uri.joinPath(directory, "temp");
// 		vscode.workspace.fs.createDirectory(temp_dir);
// 		return vscode.Uri.joinPath(temp_dir, "temp-" +  diagram_name + "." + this.format.toLowerCase());
// 	}

// }
