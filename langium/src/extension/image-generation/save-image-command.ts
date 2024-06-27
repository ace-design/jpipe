import * as vscode from 'vscode';
import { Format } from './image-generator.js';

//creates the command required to run to generate the image
export class SaveImageCommand{
	private jar_file: vscode.Uri; //location of the jar file
	private log_level: string; //log level setting

    private editor!: vscode.TextEditor;
    private document!: vscode.TextDocument;
    private directory!: vscode.WorkspaceFolder;

	constructor(context: vscode.ExtensionContext, editor: vscode.TextEditor | undefined){
        //the extension context should not change
        this.jar_file = vscode.Uri.joinPath(context.extensionUri, 'jar', 'jpipe.jar');
        
        //the log level should only change on configuration change
        this.log_level = "all";

        //automatically registers to start
        this.updateEditor(editor);
	}

    //updates each time the editor changes
    public updateEditor(editor: vscode.TextEditor | undefined){
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

	public async makeCommand(format: Format): Promise<string>{
		// Store the path to the jd file that needs to be compiled.
		let input_file = this.document.uri;
		
		let diagram_name = this.getDiagramName();

		let output_file = await this.makeOutputPath(diagram_name, format);
		
		return 'java -jar ' + this.jar_file.path + ' -i ' + input_file.path + ' -d '+ diagram_name + ' --format ' + format + ' --log-level ' + this.log_level + ' -o ' + output_file.path;
	}

	//helper function to get the diagram name from the document
	private getDiagramName(): string{
		let diagram_name: string | undefined;
		let match: RegExpExecArray | null;
		let i = 0;
	
		let lines = this.document.getText().split("\n");
		let line_num = this.editor.selection.active.line + 1;

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

    private async makeOutputPath(diagram_name: string, format: Format): Promise<vscode.Uri>{
        let default_output_file =vscode.Uri.joinPath(this.directory.uri, diagram_name + "." + format.toLowerCase());

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
