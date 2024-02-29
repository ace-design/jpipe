import * as vscode from 'vscode'
import util from "node:util";




export class editorReader implements vscode.CustomTextEditorProvider {

    constructor(
		private readonly context: vscode.ExtensionContext
	) { }

	private static viewType = "jpipe.vis";
	private static data = "NO DATA";
	private static output_channel = vscode.window.createOutputChannel("output_channel");

    public static register(context: vscode.ExtensionContext): vscode.Disposable {
		vscode.commands.registerCommand(editorReader.viewType, () => {});
		const provider = new editorReader(context);
		const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.viewType, provider);
		return providerRegistration;

	}
	
    public async resolveCustomTextEditor(
		document: vscode.TextDocument,
		webviewPanel: vscode.WebviewPanel,
		_token: vscode.CancellationToken
	): Promise<void> {
		// Setup initial content for the webview
		webviewPanel = vscode.window.createWebviewPanel(
			'VisCoding', // Identifies the type of the webview. Used internally
			'VisCoding', // Title of the panel displayed to the user
			vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
			{} // Webview options. More on these later.
		  );

		this.updateSVG(webviewPanel.webview, document);
		webviewPanel.webview.html = this.getHtmlForWebview();

		const updateWebview = async () => {
			await this.updateSVG(webviewPanel.webview, document);
			// editorReader.output_channel.appendLine("Updated SVG")
			webviewPanel.webview.html = this.getHtmlForWebview();
			editorReader.output_channel.appendLine("Updated HTML")

		}

		// Hook up event handlers so that we can synchronize the webview with the text document.
		//
		// The text document acts as our model, so we have to sync change in the document to our
		// editor and sync changes in the editor back to the document.
		// 
		// Remember that a single text document can also be shared between multiple custom
		// editors (this happens for example when you split a custom editor)

		const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(e => {
			if (e.document.uri.toString() === document.uri.toString()) {
				editorReader.output_channel.appendLine("Document Changed! Will update now")
				updateWebview();
			}
		});

		// Make sure we get rid of the listener when our editor is closed.
		webviewPanel.onDidDispose(() => {
			changeDocumentSubscription.dispose();
		});

		updateWebview();
	}


	private async updateSVG(webview: vscode.Webview, document: vscode.TextDocument): Promise<void> {
		const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, '../jpipe.jar')).path.toString()
		const fileExt = document.uri.path.toString()

		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);


		// Waits for the result

		try{
			const {stdout, stderr} = await execPromise('java -jar '+jarExt+' -i '+fileExt+ ' --format SVG');
			editorReader.data = stdout;
		} catch (error: any){
			editorReader.output_channel.appendLine(error.toString())
			// vscode.window.showErrorMessage(error.toString());
		}

		editorReader.output_channel.appendLine("Executed Jar")

		// try{
		// 	const {stdout, stderr} = await execPromise('cat '+this.context.extensionUri.path.toString()+'/output'+'/*.svg');
		// 	editorReader.data = stdout;
		// } catch (error: any){
		// 	editorReader.output_channel.appendLine(error.toString())
		// 	// vscode.window.showErrorMessage(error.toString());
		// }
		// editorReader.output_channel.appendLine("Catting output")

		// try{
		// 	const {stdout, stderr} = await execPromise('rm -r '+this.context.extensionUri.path.toString()+'/output');
		// } catch (error: any){
		// 	editorReader.output_channel.appendLine(error.toString())
		// 	// vscode.window.showErrorMessage(error.toString());
		// }
		// editorReader.output_channel.appendLine("Deleting Files")


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


    


}