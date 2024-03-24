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
    // Defines the command needed to execute the extension. 
    static ext_command = "jpipe.vis";
    // Stores the svg code to display.
    static svg_data;
    // New channel created in vscode terminal for user debugging.
    static output_channel;
    // Used to prevent jar files from executing concurrently.
    static updating;
    // Global webview panel used to display any and all svgs.
    static webviewPanel;
    // Used to determine whether webview was closed by user.
    static webviewDisposed;
    // Global text panel used to display the jd code. 
    static textPanel;
    // Global variable determining which line the user is on in the text panel. 
    static line_num;
    constructor(context) {
        this.context = context;
        // Without any initial data, must be empty string to prevent null error. 
        editorReader.svg_data = "";
        editorReader.output_channel = vscode.window.createOutputChannel("output_channel");
        editorReader.updating = false;
        editorReader.webviewPanel = vscode.window.createWebviewPanel('SVG', // Identifies the type of the webview. Used internally
        'VisCoding', // Title of the panel displayed to the user
        vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
        {});
        editorReader.webviewDisposed = false;
    }
    // Activate an extension instance. The same instance will be used for any jd file that is opened later on.
    static register(context) {
        vscode.commands.registerCommand(editorReader.ext_command, () => { });
        const provider = new editorReader(context);
        const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.ext_command, provider);
        return providerRegistration;
    }
    async resolveCustomTextEditor(document, webviewPanel, _token) {
        // Open the text of the jd file. Will be fired every time a new text editor is opened.
        editorReader.textPanel = vscode.window.showTextDocument(document, vscode.ViewColumn.One, false);
        // If previous global webview id disposed, create a new one.
        if (editorReader.webviewDisposed) {
            editorReader.webviewPanel = vscode.window.createWebviewPanel('SVG', // Identifies the type of the webview. Used internally
            'VisCoding', // Title of the panel displayed to the user
            vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
            {});
            editorReader.webviewDisposed = false;
        }
        // Set the webview of this custom editor to be the global webview.
        webviewPanel = editorReader.webviewPanel;
        // Facilitates the process of changing the webview on changes. 
        const updateWebview = async () => {
            editorReader.updating = true;
            editorReader.line_num = (await editorReader.textPanel).selection.active.line + 1;
            await this.updateSVG(webviewPanel.webview, document);
            webviewPanel.webview.html = this.getHtmlForWebview();
            editorReader.output_channel.appendLine("Updated HTML to most recent code");
            editorReader.updating = false;
        };
        // Event handler executing update webview every time the text document
        const changeDocumentSubscription = vscode.workspace.onDidChangeTextDocument(async (e) => {
            if (e.document.uri.toString() === document.uri.toString()) {
                if (editorReader.updating == false) {
                    editorReader.output_channel.appendLine("Document Changed! Will execute jar file again.");
                    await updateWebview();
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
    // Executes the jar file for updated SVG
    async updateSVG(webview, document) {
        // Store the path to the jar executable file.
        const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, 'src/utils/jpipe.jar')).path.toString();
        // Store the path to the jd file that needs to be compiled.
        const fileExt = document.uri.path.toString();
        const { exec } = require('node:child_process');
        const execPromise = node_util_1.default.promisify(exec);
        let diagram_name = this.getDiagramName(document);
        editorReader.webviewPanel.title = diagram_name || "visCoding";
        let command = 'java -jar ' + jarExt + ' -i ' + fileExt + ' -d ' + diagram_name + ' --format SVG --log-level all';
        // Execute the command, and wait for the result (must be synchronous).
        // TODO: Validate that this actually executes synchronously. 
        try {
            const { stdout, stderr } = await execPromise(command);
            editorReader.output_channel.appendLine(stderr.toString());
            editorReader.svg_data = stdout;
        }
        catch (error) {
            editorReader.output_channel.appendLine(error.toString());
        }
        editorReader.output_channel.appendLine("Executed Jar");
    }
    // Determine which diagram of the document the user is on. 
    getDiagramName(document) {
        let diagram_name = null;
        let match = null;
        let lines = document.getText().split("\n");
        for (let i = 0; i < lines.length; i++) {
            match = /justification .*/i.exec(lines[i]) || /pattern .*/i.exec(lines[i]);
            if (match && (i < editorReader.line_num || diagram_name === null)) {
                diagram_name = match[0].split(' ')[1];
            }
            if (i >= editorReader.line_num && diagram_name !== null) {
                break;
            }
        }
        return diagram_name;
    }
    // Event handler determining what the next active text editor is (when the user switched tabs).
    changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async (e) => {
        if (e !== undefined && e.document.languageId == "jpipe") {
            editorReader.textPanel = vscode.window.showTextDocument(e.document, vscode.ViewColumn.One, false);
            editorReader.line_num = (await editorReader.textPanel).selection.active.line + 1;
            editorReader.webviewPanel.webview.html = this.getLoadingHTMLWebview();
            let token = new vscode.CancellationTokenSource();
            this.resolveCustomTextEditor(e.document, editorReader.webviewPanel, token.token);
        }
    });
    changeDocumentSelection = vscode.window.onDidChangeTextEditorSelection(async (e) => {
        if (e !== undefined && e.textEditor.document.languageId == "jpipe") {
            editorReader.line_num = (await editorReader.textPanel).selection.active.line + 1;
            let new_diagram = this.getDiagramName(e.textEditor.document);
            let token = new vscode.CancellationTokenSource();
            if (new_diagram != editorReader.webviewPanel.title) {
                this.resolveCustomTextEditor(e.textEditor.document, editorReader.webviewPanel, token.token);
            }
        }
    });
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
		  <div>${editorReader.svg_data}</div>
		</body>
		</html>
		`;
    }
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
		  <div>${editorReader.svg_data}</div>
		</body>
		</html>		
		`;
    }
}
exports.editorReader = editorReader;
//# sourceMappingURL=editorReader.js.map