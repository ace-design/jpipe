import * as vscode from 'vscode';
import { Format, ImageGenerator } from './image-generator.js';
import { JPipeOutput, OutputManager, EventSubscriber, isTextEditor, isTextEditorSelectionChangeEvent, Command, CommandUser } from '../managers/index.js';
import { HTMLProvider } from './html-provider.js';


//altered from editorReader
export class PreviewProvider implements vscode.CustomTextEditorProvider, CommandUser, EventSubscriber<vscode.TextEditor | undefined>, EventSubscriber<vscode.TextEditorSelectionChangeEvent> {

	// Defines the command needed to execute the extension. 
	private static ext_command = "jpipe.vis";

	private static ext_command_preview = "jpipe.vis.preview";

	// Stores the svg code to display.
	protected static svg_data: string;

	// Used to prevent jar files from executing concurrently.
	private static updating: boolean;

	// Global webview panel used to display any and all svgs.
	protected static webviewPanel: vscode.WebviewPanel;

	// Used to determine whether webview was closed by user.
	private static webviewDisposed: boolean;

	// Global text panel used to display the jd code. 
	private static textPanel: Thenable<vscode.TextEditor>;

    private static image_generator: ImageGenerator;

	//private static HTMLProvider: HTMLProvider;

    constructor(image_generator: ImageGenerator, private readonly output_manager: OutputManager, private readonly context: vscode.ExtensionContext) {
		// Without any initial data, must be empty string to prevent null error. 
		PreviewProvider.svg_data = "";
		PreviewProvider.updating = false;
		PreviewProvider.webviewDisposed = true;
        PreviewProvider.image_generator = image_generator;

		vscode.window.registerCustomEditorProvider(PreviewProvider.ext_command, this);
		PreviewProvider.textPanel;
	}

	public getCommands(): Command[] | Command{
		return {command: PreviewProvider.ext_command_preview, callback: () => this.createWebview()};
	}

	//updates webview on change events
	public async update(editor: vscode.TextEditor | undefined): Promise<void>;
    public async update(changes: vscode.TextEditorSelectionChangeEvent): Promise<void>;
    public async update(data: (vscode.TextEditor | undefined) | vscode.TextEditorSelectionChangeEvent): Promise<void>{
        if(!PreviewProvider.webviewDisposed){
			if(isTextEditorSelectionChangeEvent(data)){
				this.updateTextSelection(data);
			}
			else if(isTextEditor(data)){
				this.updateEditor(data);
			}
		}
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
			
			PreviewProvider.webviewPanel.webview.onDidReceiveMessage(
				message => {
				  switch (message.command) {
					case 'handle_click':
					  vscode.window.showErrorMessage(message.text);
					  return;
				  }
				},
				undefined,
				this.context.subscriptions
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
			this.output_manager.log(JPipeOutput.CONSOLE, "Updated HTML to most recent code"); //user information
			PreviewProvider.updating = false;
		}

		// Event handler executing update webview every time the text document
		const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async e => {
			if (e.document.uri.toString() === document.uri.toString()) {
				if (PreviewProvider.updating==false){
					this.output_manager.log(JPipeOutput.USER, "Document Changed! Will execute jar file again.");
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
		try{
			const {stdout} = await PreviewProvider.image_generator.generate({format: Format.SVG, save_image: false})
			
			PreviewProvider.webviewPanel.title = PreviewProvider.image_generator.getDiagramName();
			PreviewProvider.svg_data = stdout;
			this.output_manager.log(JPipeOutput.IMAGE, stdout +"\n\n");

		}catch (error: any){
			this.output_manager.log(JPipeOutput.USER, "Error found!");//logs to both
			this.output_manager.log(JPipeOutput.CONSOLE, "[Preview Provider]" + error.toString());
		}

		this.output_manager.log(JPipeOutput.CONSOLE, "Executed Jar");
    }	

	//helper function to update editor
	private async updateEditor(editor: vscode.TextEditor | undefined){
		if (editor !== undefined && editor.document.languageId=="jpipe" && !PreviewProvider.webviewDisposed){
			try{
				PreviewProvider.textPanel = vscode.window.showTextDocument(editor.document, vscode.ViewColumn.One, true);
				PreviewProvider.webviewPanel.webview.html = PreviewProvider.getLoadingHTMLWebview();
				let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
				this.resolveCustomTextEditor(editor.document, PreviewProvider.webviewPanel, token.token);
			}
			catch(error: any){
				this.output_manager.log(JPipeOutput.USER, "Image cannot be loaded due to: ");
				this.output_manager.log(JPipeOutput.USER, error.toString());

				this.output_manager.log(JPipeOutput.CONSOLE, "Image cannot be loaded due to: ");
				this.output_manager.log(JPipeOutput.CONSOLE, error.toString());
				
				PreviewProvider.webviewPanel.webview.html =  `
				<!DOCTYPE html>
				<html lang="en">
				<head>
				</head>
				`;
			}
		}
	}

	//helper function to update text selection
	private async updateTextSelection(event: vscode.TextEditorSelectionChangeEvent){
		if (event !== undefined && event.textEditor.document.languageId=="jpipe" && !PreviewProvider.webviewDisposed){
			let new_diagram = PreviewProvider.image_generator.getDiagramName();
			
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			
			if (new_diagram!=PreviewProvider.webviewPanel.title){
				this.resolveCustomTextEditor(event.textEditor.document, PreviewProvider.webviewPanel, token.token)
			}
		}
	}

	// HTML Code for the webview.
	private static getHtmlForWebview(): string {
		return HTMLProvider.getHtmlForWebview(PreviewProvider.svg_data);
	}


	private static getLoadingHTMLWebview(): string {
		return HTMLProvider.getLoadingHTMLWebview();
	}

}