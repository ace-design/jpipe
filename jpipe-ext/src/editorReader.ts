import * as vscode from 'vscode'
import util from "node:util";




export class editorReader implements vscode.CustomTextEditorProvider {

	// Defines the command needed to execute the extension. 
	private static ext_command = "jpipe.vis";

	private static ext_command_preview = "jpipe.vis.preview";


	// Stores the svg code to display.
	private static svg_data: string;
	
	// New channel created in vscode terminal for user debugging.
	private static output_channel: vscode.OutputChannel

	// Used to prevent jar files from executing concurrently.
	private static updating: boolean;

	// Global webview panel used to display any and all svgs.
	private static webviewPanel: vscode.WebviewPanel;

	// Used to determine whether webview was closed by user.
	private static webviewDisposed: boolean;

	// Global text panel used to display the jd code. 
	private static textPanel: Thenable<vscode.TextEditor>;

	// Global variable determining which line the user is on in the text panel. 
	private static line_num: number; 


    constructor( private readonly context: vscode.ExtensionContext) {
		// Without any initial data, must be empty string to prevent null error. 
		editorReader.svg_data = "";
		editorReader.output_channel = vscode.window.createOutputChannel("jpipe_console");
		editorReader.updating = false;
		editorReader.webviewDisposed = true;
	 }


	// Activate an extension instance. The same instance will be used for any jd file that is opened later on.
    public static register(context: vscode.ExtensionContext): vscode.Disposable {
		const provider = new editorReader(context);
		const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.ext_command, provider);

		vscode.commands.registerCommand(editorReader.ext_command_preview, () => {
			// If previous global webview id disposed, create a new one.
			if (editorReader.webviewDisposed){
				editorReader.webviewPanel = vscode.window.createWebviewPanel(
					'SVG', // Identifies the type of the webview. Used internally
					'VisCoding', // Title of the panel displayed to the user
					{
						viewColumn: vscode.ViewColumn.Beside,
						preserveFocus: true
					},
					{}
				);
				editorReader.webviewPanel.webview.html = this.getHtmlForWebview();
				editorReader.webviewDisposed = false;
			} else {
				editorReader.webviewPanel.dispose();
				editorReader.webviewDisposed = true;
			}
		});

		vscode.commands.registerCommand(editorReader.ext_command, () => {})

		return providerRegistration;
	}
	
    public async resolveCustomTextEditor(
		document: vscode.TextDocument,
		webviewPanel: vscode.WebviewPanel,
		_token: vscode.CancellationToken,
	): Promise<void> {

		// Open the text of the jd file. Will be fired every time a new text editor is opened.
		editorReader.textPanel = vscode.window.showTextDocument(document, vscode.ViewColumn.One, true);


		// If previous global webview id disposed, create a new one.
		if (editorReader.webviewDisposed){
			editorReader.webviewPanel = vscode.window.createWebviewPanel(
				'SVG', // Identifies the type of the webview. Used internally
				'VisCoding', // Title of the panel displayed to the user
				{
					viewColumn: vscode.ViewColumn.Beside,
					preserveFocus: true
				},
				{}
			);
			editorReader.webviewDisposed = false;
		}

		// Set the webview of this custom editor to be the global webview.
		webviewPanel = editorReader.webviewPanel;


		// Facilitates the process of changing the webview on changes. 
		const updateWebview = async () => {
			editorReader.updating = true;
			editorReader.line_num = (await editorReader.textPanel).selection.active.line+1
			await this.updateSVG(webviewPanel.webview, document);
			webviewPanel.webview.html = editorReader.getHtmlForWebview();
			editorReader.output_channel.appendLine("Updated HTML to most recent code")
			editorReader.updating = false;

		}

		// Event handler executing update webview every time the text document
		const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async e => {
			if (e.document.uri.toString() === document.uri.toString()) {
				if (editorReader.updating==false){
					editorReader.output_channel.appendLine("Document Changed! Will execute jar file again.")
					await updateWebview();
				}
			}
		});
	

		// Make sure we get rid of the listener when our editor is closed. 
		editorReader.webviewPanel.onDidDispose(() => {
			changeDocumentSubscription.dispose();
			// this.changeDocumentSelection.dispose();
			// this.changeDocumentSubscription.dispose();
			editorReader.webviewDisposed=true;
		});


		updateWebview();
	}

	// Executes the jar file for updated SVG
	public async updateSVG(webview: vscode.Webview, document: vscode.TextDocument): Promise<void> {
		// Store the path to the jar executable file.
		const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, './jpipe.jar')).path.toString()

		// Store the path to the jd file that needs to be compiled.
		const fileExt = document.uri.path.toString()

		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);

		
		let diagram_name = this.getDiagramName(document);
		editorReader.webviewPanel.title=diagram_name || "visCoding";
		let command = 'java -jar '+jarExt+' -i '+fileExt+' -d '+diagram_name+ ' --format SVG --log-level all'

		// Execute the command, and wait for the result (must be synchronous).
		// TODO: Validate that this actually executes synchronously. 
		try{
			const {stdout, stderr} = await execPromise(command);
			editorReader.output_channel.appendLine(stderr.toString());
			editorReader.svg_data = stdout;
		} catch (error: any){
			editorReader.output_channel.appendLine(error.toString());
		}

		editorReader.output_channel.appendLine("Executed Jar")

	}

	// Determine which diagram of the document the user is on. 
	private getDiagramName(document: vscode.TextDocument): string | null{
		let diagram_name = null;
		let match = null;

		let lines = document.getText().split("\n");

		for (let i = 0; i < lines.length; i++)
		{
			match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
			if (match && (i<editorReader.line_num || diagram_name===null))
			{
				diagram_name = match[0].split(' ')[1]
			}
			if (i>=editorReader.line_num && diagram_name!==null){
				break;
			}
		}
		return diagram_name;
	}


	// Event handler determining what the next active text editor is (when the user switched tabs).
	changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async e => {
		if (e !== undefined && e.document.languageId=="jpipe" && !editorReader.webviewDisposed){
			editorReader.textPanel = vscode.window.showTextDocument(e.document, vscode.ViewColumn.One, true);
			editorReader.line_num = (await editorReader.textPanel).selection.active.line+1
			editorReader.webviewPanel.webview.html = editorReader.getLoadingHTMLWebview();
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			this.resolveCustomTextEditor(e.document, editorReader.webviewPanel, token.token)
		}
	});

	changeDocumentSelection = vscode.window.onDidChangeTextEditorSelection(async e => {
		if (e !== undefined && e.textEditor.document.languageId=="jpipe" && !editorReader.webviewDisposed){
			editorReader.line_num = (await editorReader.textPanel).selection.active.line+1;
			let new_diagram = this.getDiagramName(e.textEditor.document)
			let token : vscode.CancellationTokenSource = new vscode.CancellationTokenSource();
			if (new_diagram!=editorReader.webviewPanel.title){
				this.resolveCustomTextEditor(e.textEditor.document, editorReader.webviewPanel, token.token)
			}
		}
	});


private static getHtmlForWebview(): string {
    const svgContent = editorReader.svg_data || ''; // Ensure svg_data is not null or undefined

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
		  <div>${editorReader.svg_data}</div>
		</body>
		</html>		
		`
	}

}