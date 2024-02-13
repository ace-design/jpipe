import * as vscode from 'vscode'
import util from "node:util";




export class editorReader implements vscode.CustomTextEditorProvider {

    constructor(
		private readonly context: vscode.ExtensionContext
	) { }

	private static viewType = "jpipe.vis";
	private static data = "NO DATA";

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

		const updateWebview = () => {
			this.updateSVG(webviewPanel.webview, document);
			webviewPanel.webview.html = this.getHtmlForWebview();
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
				vscode.window.showInformationMessage("UPDATINGGG!!!!");
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
			const {stdout, stderr} = await execPromise('java -jar '+jarExt+' -i '+fileExt+ ' --format svg -o '+this.context.extensionUri.path.toString()+'/output');
			// editorReader.data = stdout;
		} catch (error: any){
			vscode.window.showErrorMessage(error.toString());
		}

		try{
			const {stdout, stderr} = await execPromise('cat '+this.context.extensionUri.path.toString()+'/output'+'/simple_prove_models.svg');
			editorReader.data = stdout;
		} catch (error: any){
			vscode.window.showErrorMessage(error.toString());
		}

		

		//Doesn't wait for the result.

		// exec(('java -jar '+jarExt+' -i '+fileExt+ ' --format svg -o '+this.context.extensionUri.path.toString()+'/output'), (err: any, output: any) => {
		// 	if (err) {
		// 		vscode.window.showErrorMessage(err.toString());
		// 		return
		// 	}
		// 	vscode.window.showInformationMessage(output.toString());
		// })
		
		// exec(('cat '+this.context.extensionUri.path.toString()+'/output'+'/simple_prove_models.svg'), (err: any, output: any) => {
		// 	if (err) {
		// 		vscode.window.showErrorMessage(err.toString());
		// 		return
		// 	}
		// 	editorReader.data = output
		// 	vscode.window.showInformationMessage(output);
		// })
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