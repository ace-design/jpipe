import * as vscode from 'vscode';
import util from "node:util";
import { Disposable } from 'vscode-languageserver';

export class ImageGenerator{

	// Defines the command needed to execute the extension. 
	private static exe_command = "jpipe.downloadImage";
	
	// New channel created in vscode terminal for user debugging.
	private static output_channel: vscode.OutputChannel

    constructor( private readonly context: vscode.ExtensionContext) {
		ImageGenerator.output_channel = vscode.window.createOutputChannel("jpipe_image");
	 }

	 public register(): Disposable{
		vscode.commands.registerCommand(ImageGenerator.exe_command, () => {
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
        
		// Execute the command, and wait for the result (must be synchronous).
		// TODO: Validate that this actually executes synchronously. 
		try{
			let command  = this.makeCommand(document);
			const {stdout, stderr} = await execPromise(command);
			
			ImageGenerator.output_channel.appendLine(stdout.toString());
			ImageGenerator.output_channel.appendLine(stderr.toString());
		} catch (error: any){
			ImageGenerator.output_channel.appendLine(error.toString());
		}
	}

	//helper function to make the command
	private makeCommand(document: vscode.TextDocument){
		const directory = vscode.workspace.getWorkspaceFolder(document.uri)?.uri.path.toString() + "/";
		if(!directory){
			throw new Error("Current Directory not found");;
		}

		// Store the path to the jar executable file.
		const jar_file = vscode.Uri.joinPath(this.context.extensionUri, './jar/jpipe.jar').path.toString();

		// Store the path to the jd file that needs to be compiled.
		const input_file = document.uri.path.toString();

		let diagram_name = this.getDiagramName(document);

		let output_file = "smurf.png";
		let log_level = "all";
		let format="PNG";

		let command = 'java -jar ' + jar_file + ' -i ' + input_file + ' -d '+ diagram_name + ' --format ' + format + ' --log-level ' + log_level + ' -o ' + directory + output_file;
		return command;
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