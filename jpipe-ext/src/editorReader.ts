import * as vscode from 'vscode'
import util from "node:util";




export class editorReader implements vscode.CustomTextEditorProvider {

    constructor(
		private readonly context: vscode.ExtensionContext
	) { }

	private static viewType = "jpipe.vis";
	private static data = "NO DATA";
	private static output_channel = vscode.window.createOutputChannel("output_channel");
	private static updating = false;
	private static webviewPanel: vscode.WebviewPanel;
	private static webviewDisposed: boolean;
	private static activeEditor: vscode.TextDocument; 

    public static register(context: vscode.ExtensionContext): vscode.Disposable {
		vscode.commands.registerCommand(editorReader.viewType, () => {});
		const provider = new editorReader(context);
		const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.viewType, provider);
		editorReader.webviewPanel = vscode.window.createWebviewPanel(
			'SVG', // Identifies the type of the webview. Used internally
			'VisCoding', // Title of the panel displayed to the user
			vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
			{} // Webview options. More on these later.
		  );
		editorReader.webviewDisposed = false;
		return providerRegistration;
	}
	
    public async resolveCustomTextEditor(
		document: vscode.TextDocument,
		webviewPanel: vscode.WebviewPanel,
		_token: vscode.CancellationToken,
	): Promise<void> {
		// Setup initial content for the webview
		let textPanel = vscode.window.showTextDocument(document, vscode.ViewColumn.One, false);

		// document = editorReader.activeEditor;

		// If previous webview was disposed, create a new one. 
		if (editorReader.webviewDisposed){
			editorReader.webviewPanel = vscode.window.createWebviewPanel(
				'SVG', // Identifies the type of the webview. Used internally
				'VisCoding', // Title of the panel displayed to the user
				vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
				{} // Webview options. More on these later.
			);
			editorReader.webviewDisposed = false;
		}

		// Set the webview of the custom text editor to be the global webview. 
		webviewPanel = editorReader.webviewPanel;

		// this.updateSVG(webviewPanel.webview, document);
		// webviewPanel.webview.html = this.getHtmlForWebview();

		const updateWebview = async () => {
			editorReader.updating = true;
			let curr_line = (await textPanel).selection.active.line+1
			await this.updateSVG(webviewPanel.webview, document, curr_line);
			webviewPanel.webview.html = this.getHtmlForWebview();
			editorReader.output_channel.appendLine("Updated HTML to most recent code")
			editorReader.updating = false;

		}

		// Hook up event handlers so that we can synchronize the webview with the text document.
		//
		// The text document acts as our model, so we have to sync change in the document to our
		// editor and sync changes in the editor back to the document.
		// 
		// Remember that a single text document can also be shared between multiple custom
		// editors (this happens for example when you split a custom editor)

		const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async e => {
			if (e.document.uri.toString() === document.uri.toString()) {
				if (editorReader.updating==false){
					editorReader.output_channel.appendLine("Document Changed! Will execute jar file again.")
					// this.startTimer();
					await updateWebview();
					// editorReader.output_channel.appendLine("Execution Time: "+this.endTimer().toString()+"s");
				}
			}
		});
	

		// Make sure we get rid of the listener when our editor is closed.
		webviewPanel.onDidDispose(() => {
			changeDocumentSubscription.dispose();
			editorReader.webviewDisposed=true;

		});

		updateWebview();
	}

	// private startTimer(){
	// 	let date = new Date();
	// 	editorReader.start_time = date.getSeconds()+(date.getMilliseconds()/1000);
	// }

	// private endTimer(): number {
	// 	let date = new Date();
	// 	let end_time = date.getSeconds()+(date.getMilliseconds()/1000);
	// 	return (end_time-editorReader.start_time);
	// }

	private async updateSVG(webview: vscode.Webview, document: vscode.TextDocument, curr_line: number): Promise<void> {
		const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, '../jpipe.jar')).path.toString()
		const fileExt = document.uri.path.toString()

		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);


		let diagram_name = this.getDiagramName(document, curr_line);
		let command = 'java -jar '+jarExt+' -i '+fileExt+' -d '+diagram_name+ ' --format SVG --log-level all'


		// Waits for the result

		try{
			const {stdout, stderr} = await execPromise(command);
			editorReader.output_channel.appendLine(stderr.toString());
			editorReader.data = stdout;
		} catch (error: any){
			editorReader.output_channel.appendLine(error.toString());
			// vscode.window.showErrorMessage(error.toString());
		}

		editorReader.output_channel.appendLine("Executed Jar")

	}


	private getDiagramName(document: vscode.TextDocument, curr_line: number): string | null{
		let diagram_name = null;
		let match = null;

		let lines = document.getText().split("\n");

		for (let i = 0; i < lines.length; i++)
		{
			match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
			if (match && (i<curr_line || diagram_name===null))
			{
				diagram_name = match[0].split(' ')[1]
				// editorReader.output_channel.appendLine("NAMEEEEE"+diagram_name);
				// editorReader.output_channel.appendLine("LINEEEE"+curr_line);
			}
			if (i>=curr_line && diagram_name!==null){
				break;
			}
		}

		return diagram_name;
	}


    private getHtmlForWebview(): string {


		return /* html */`
			<!DOCTYPE html>
			<html lang="en">
			<body>
				${editorReader.data}
			</body>
			</html>`;	 
    }

	changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async e => {
		if (e !== undefined){
			editorReader.activeEditor = e.document;
			vscode.window.showInformationMessage(e.document.toString())
		}
	});

}