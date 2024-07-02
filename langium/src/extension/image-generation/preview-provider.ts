import * as vscode from 'vscode'
import util from "node:util";
import { SaveImageCommand } from './save-image-command.js';
import { Format } from './image-generator.js';
import { Command, CommandUser } from '../command-management/command-manager.js';

//altered from editorReader
export class PreviewProvider implements vscode.CustomTextEditorProvider, CommandUser {

	// Defines the command needed to execute the extension. 
	private static ext_command = "jpipe.vis";

	private static ext_command_preview = "jpipe.vis.preview";

	// Stores the svg code to display.
	protected static svg_data: string;
	
	// New channel created in vscode terminal for user debugging.
	static output_channel: vscode.OutputChannel

	// Used to prevent jar files from executing concurrently.
	private static updating: boolean;

	// Global webview panel used to display any and all svgs.
	protected static webviewPanel: vscode.WebviewPanel;

	// Used to determine whether webview was closed by user.
	private static webviewDisposed: boolean;

	// Global text panel used to display the jd code. 
	private static textPanel: Thenable<vscode.TextEditor>;

    private static save_image_command: SaveImageCommand;

    constructor( private readonly context: vscode.ExtensionContext, save_image_command: SaveImageCommand) {
		// Without any initial data, must be empty string to prevent null error. 
		PreviewProvider.svg_data = "";
		PreviewProvider.output_channel = vscode.window.createOutputChannel("jpipe_console");
		PreviewProvider.updating = false;
		PreviewProvider.webviewDisposed = true;
        PreviewProvider.save_image_command = save_image_command;

		vscode.window.registerCustomEditorProvider(PreviewProvider.ext_command, this);
	}

	public getCommands(): Command[] | Command{
		return {command: PreviewProvider.ext_command_preview, callback: () => this.createWebview()};
	}

	private async createWebview(): Promise<void>{
		// If previous global webview id disposed, create a new one.
		if (PreviewProvider.webviewDisposed){
			PreviewProvider.webviewPanel = vscode.window.createWebviewPanel(
				'SVG', // Identifies the type of the webview. Used internally
				'Diagram Preview', // Title of the panel displayed to the user
				{
					viewColumn: vscode.ViewColumn.Beside,
					preserveFocus: true
				},
				{}
			);
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			PreviewProvider.webviewDisposed = false;

			if(vscode.window.activeTextEditor){
				await this.resolveCustomTextEditor(vscode.window.activeTextEditor.document, PreviewProvider.webviewPanel, token.token)
			}
			
			PreviewProvider.webviewPanel.webview.html = PreviewProvider.getHtmlForWebview();
		} else {
			PreviewProvider.webviewPanel.dispose();
			PreviewProvider.webviewDisposed = true;
		}
	}

    public async resolveCustomTextEditor(
		document: vscode.TextDocument,
		webviewPanel: vscode.WebviewPanel,
		_token: vscode.CancellationToken,
	): Promise<void> {
		// Open the text of the jd file. Will be fired every time a new text editor is opened.
		PreviewProvider.textPanel = vscode.window.showTextDocument(document, vscode.ViewColumn.One, true);

		// If previous global webview id disposed, create a new one.
		if (PreviewProvider.webviewDisposed){
			PreviewProvider.webviewPanel = vscode.window.createWebviewPanel(
				'SVG', // Identifies the type of the webview. Used internally
				'Diagram Preview', // Title of the panel displayed to the user
				{
					viewColumn: vscode.ViewColumn.Beside,
					preserveFocus: true
				},
				{}
			);
			PreviewProvider.webviewDisposed = false;
		}

		// Set the webview of this custom editor to be the global webview.
		webviewPanel = PreviewProvider.webviewPanel;

		// Facilitates the process of changing the webview on changes. 
		const updateWebview = async () => {
			PreviewProvider.updating = true;
			await this.updateSVG();
			
			webviewPanel.webview.html = PreviewProvider.getHtmlForWebview();
			PreviewProvider.output_channel.appendLine("Updated HTML to most recent code");
			PreviewProvider.updating = false;
		}

		// Event handler executing update webview every time the text document
		const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async e => {
			if (e.document.uri.toString() === document.uri.toString()) {
				if (PreviewProvider.updating==false){
					PreviewProvider.output_channel.appendLine("Document Changed! Will execute jar file again.")
					await updateWebview();
				}
			}
		});
	

		// Make sure we get rid of the listener when our editor is closed. 
		PreviewProvider.webviewPanel.onDidDispose(() => {
			changeDocumentSubscription.dispose();
			// this.changeDocumentSelection.dispose();
			// this.changeDocumentSubscription.dispose();
			PreviewProvider.webviewDisposed=true;
		});


		updateWebview();
	}

	// Executes the jar file for updated SVG
	public async updateSVG(): Promise<void> {
        const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);
		
		let command = await PreviewProvider.save_image_command.makeCommand({format: Format.SVG, save_image: false});

		PreviewProvider.webviewPanel.title = PreviewProvider.save_image_command.getDiagramName();
		
		try{
			const {stdout, stderr} = await execPromise(command);
			PreviewProvider.output_channel.appendLine(stderr.toString());
			PreviewProvider.svg_data = stdout;
		} catch (error: any){
			PreviewProvider.output_channel.appendLine(error.toString());
		}

		PreviewProvider.output_channel.appendLine("Executed Jar");
    }

	
	public async updateEditor(editor: vscode.TextEditor | undefined){
		if (editor !== undefined && editor.document.languageId=="jpipe" && !PreviewProvider.webviewDisposed){
			PreviewProvider.textPanel = vscode.window.showTextDocument(editor.document, vscode.ViewColumn.One, true);
			PreviewProvider.webviewPanel.webview.html = PreviewProvider.getLoadingHTMLWebview();
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			this.resolveCustomTextEditor(editor.document, PreviewProvider.webviewPanel, token.token)
		}
	}


	public async updateTextSelection(event: vscode.TextEditorSelectionChangeEvent){
		if (event !== undefined && event.textEditor.document.languageId=="jpipe" && !PreviewProvider.webviewDisposed){
			let new_diagram = PreviewProvider.save_image_command.getDiagramName();
			
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			
			if (new_diagram!=PreviewProvider.webviewPanel.title){
				this.resolveCustomTextEditor(event.textEditor.document, PreviewProvider.webviewPanel, token.token)
			}
		}
	}
	
	// Event handler determining what the next active text editor is (when the user switched tabs).
	changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async e => {
		this.updateEditor(e);
	});

	// Event handler for determinining which diagram the user is on in the text editor. 
	changeDocumentSelection = vscode.window.onDidChangeTextEditorSelection(async e => {
		this.updateTextSelection(e);
	});

// HTML Code for the webview.

private static getHtmlForWebview(): string {
	if(PreviewProvider.textPanel === undefined && vscode.window.activeTextEditor){
		PreviewProvider.textPanel = vscode.window.showTextDocument(vscode.window.activeTextEditor.document, vscode.ViewColumn.One, true);
	}

    const svgContent = PreviewProvider.svg_data || ''; // Ensure svg_data is not null or undefined
    return /* html */`
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>SVG Viewer</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                    background-color: #f0f0f0;
                }
                #svg-container {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 100%;
                    height: 100%;
                }
                svg {
                    max-width: 100%;
                    max-height: 100%;
                    width: auto;
                    height: auto;
                }
            </style>
        </head>
        <body>
            <div id="svg-container">
                ${svgContent}
            </div>
        </body>
        </html>
    `;
}


	private static getLoadingHTMLWebview(): string {
		return `
		<!DOCTYPE html>
		<html lang="en">
		<head>
		  <meta charset="UTF-8">
		  <meta name="viewport" content="width=device-width, initial-scale=1.0">
		  <title>Loading Page</title>
		  <style>
			body {
			  margin: 0;
			  padding: 0;
			  position: relative; /* Add this line */
			  display: flex;
			  justify-content: center;
			  align-items: center;
			  height: 100vh;
			  background-color: #f0f0f0;
			}
			.loader {
			  position: absolute; /* Add this line */
			  top: 50%; /* Add this line */
			  left: 50%; /* Add this line */
			  transform: translate(-50%, -50%); /* Add this line */
			  border: 8px solid #f3f3f3;
			  border-top: 8px solid #3498db;
			  border-radius: 50%;
			  width: 50px;
			  height: 50px;
			  animation: spin 1s linear infinite;
			}
			@keyframes spin {
			  0% { transform: rotate(0deg); }
			  100% { transform: rotate(360deg); }
			}
		  </style>
		</head>
		<body>
		  <div class="loader"></div>
		  <div>${PreviewProvider.svg_data}</div>
		</body>
		</html>		
		`
	}

}