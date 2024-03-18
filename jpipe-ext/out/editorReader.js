"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.editorReader = void 0;
const vscode = __importStar(require("vscode"));
const node_util_1 = __importDefault(require("node:util"));
class editorReader {
    context;
    constructor(context) {
        this.context = context;
    }
    static viewType = "jpipe.vis";
    static data = "NO DATA";
    static output_channel = vscode.window.createOutputChannel("output_channel");
    static updating = false;
    static webviewPanel;
    static webviewDisposed;
    static register(context) {
        vscode.commands.registerCommand(editorReader.viewType, () => { });
        const provider = new editorReader(context);
        const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.viewType, provider);
        editorReader.webviewPanel = vscode.window.createWebviewPanel('SVG', // Identifies the type of the webview. Used internally
        'VisCoding', // Title of the panel displayed to the user
        vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
        {} // Webview options. More on these later.
        );
        editorReader.webviewDisposed = false;
        return providerRegistration;
    }
    async resolveCustomTextEditor(document, webviewPanel, _token) {
        // Setup initial content for the webview
        let textPanel = vscode.window.showTextDocument(document, vscode.ViewColumn.One, false);
        // document = editorReader.activeEditor;
        // If previous webview was disposed, create a new one. 
        if (editorReader.webviewDisposed) {
            editorReader.webviewPanel = vscode.window.createWebviewPanel('SVG', // Identifies the type of the webview. Used internally
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
            let curr_line = (await textPanel).selection.active.line + 1;
            await this.updateSVG(webviewPanel.webview, document, curr_line);
            webviewPanel.webview.html = this.getHtmlForWebview();
            editorReader.output_channel.appendLine("Updated HTML to most recent code");
            editorReader.updating = false;
        };
        // Hook up event handlers so that we can synchronize the webview with the text document.
        //
        // The text document acts as our model, so we have to sync change in the document to our
        // editor and sync changes in the editor back to the document.
        // 
        // Remember that a single text document can also be shared between multiple custom
        // editors (this happens for example when you split a custom editor)
        const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async (e) => {
            if (e.document.uri.toString() === document.uri.toString()) {
                if (editorReader.updating == false) {
                    editorReader.output_channel.appendLine("Document Changed! Will execute jar file again.");
                    // this.startTimer();
                    await updateWebview();
                    // editorReader.output_channel.appendLine("Execution Time: "+this.endTimer().toString()+"s");
                }
            }
        });
        // Make sure we get rid of the listener when our editor is closed.
        webviewPanel.onDidDispose(() => {
            changeDocumentSubscription.dispose();
            editorReader.webviewDisposed = true;
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
    async updateSVG(webview, document, curr_line) {
        const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, '../jpipe.jar')).path.toString();
        const fileExt = document.uri.path.toString();
        const { exec } = require('node:child_process');
        const execPromise = node_util_1.default.promisify(exec);
        let diagram_name = this.getDiagramName(document, curr_line);
        editorReader.webviewPanel.title = diagram_name || "visCoding";
        let command = 'java -jar ' + jarExt + ' -i ' + fileExt + ' -d ' + diagram_name + ' --format SVG --log-level all';
        // Waits for the result
        try {
            const { stdout, stderr } = await execPromise(command);
            editorReader.output_channel.appendLine(stderr.toString());
            editorReader.data = stdout;
        }
        catch (error) {
            editorReader.output_channel.appendLine(error.toString());
            // vscode.window.showErrorMessage(error.toString());
        }
        editorReader.output_channel.appendLine("Executed Jar");
    }
    getDiagramName(document, curr_line) {
        let diagram_name = null;
        let match = null;
        let lines = document.getText().split("\n");
        for (let i = 0; i < lines.length; i++) {
            match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
            if (match && (i < curr_line || diagram_name === null)) {
                diagram_name = match[0].split(' ')[1];
                // editorReader.output_channel.appendLine("NAMEEEEE"+diagram_name);
                // editorReader.output_channel.appendLine("LINEEEE"+curr_line);
            }
            if (i >= curr_line && diagram_name !== null) {
                break;
            }
        }
        return diagram_name;
    }
    getHtmlForWebview() {
        return /* html */ `
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
		  </style>
		</head>
		<body>
		  <div>${editorReader.data}</div>
		</body>
		</html>
		`;
    }
    changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async (e) => {
        if (e !== undefined) {
            editorReader.webviewPanel.webview.html = this.getLoadingHTMLWebview();
            let token = new vscode.CancellationTokenSource();
            this.resolveCustomTextEditor(e.document, editorReader.webviewPanel, token.token);
        }
    });
    getLoadingHTMLWebview() {
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
		  <div>${editorReader.data}</div>
		</body>
		</html>		
		`;
    }
}
exports.editorReader = editorReader;
//# sourceMappingURL=editorReader.js.map