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
    static register(context) {
        vscode.commands.registerCommand(editorReader.viewType, () => { });
        const provider = new editorReader(context);
        const providerRegistration = vscode.window.registerCustomEditorProvider(editorReader.viewType, provider);
        return providerRegistration;
    }
    async resolveCustomTextEditor(document, webviewPanel, _token) {
        // Setup initial content for the webview
        webviewPanel = vscode.window.createWebviewPanel('VisCoding', // Identifies the type of the webview. Used internally
        'VisCoding', // Title of the panel displayed to the user
        vscode.ViewColumn.Two, // Editor column to show the new webview panel in.
        {} // Webview options. More on these later.
        );
        this.updateSVG(webviewPanel.webview, document);
        webviewPanel.webview.html = this.getHtmlForWebview();
        const updateWebview = () => {
            this.updateSVG(webviewPanel.webview, document);
            webviewPanel.webview.html = this.getHtmlForWebview();
        };
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
    async updateSVG(webview, document) {
        const jarExt = webview.asWebviewUri(vscode.Uri.joinPath(this.context.extensionUri, '../jpipe.jar')).path.toString();
        const fileExt = document.uri.path.toString();
        const { exec } = require('node:child_process');
        const execPromise = node_util_1.default.promisify(exec);
        // Waits for the result
        try {
            const { stdout, stderr } = await execPromise('java -jar ' + jarExt + ' -i ' + fileExt + ' --format svg -o ' + this.context.extensionUri.path.toString() + '/output');
            // editorReader.data = stdout;
        }
        catch (error) {
            vscode.window.showErrorMessage(error.toString());
        }
        try {
            const { stdout, stderr } = await execPromise('cat ' + this.context.extensionUri.path.toString() + '/output' + '/simple_prove_models.svg');
            editorReader.data = stdout;
        }
        catch (error) {
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
    getHtmlForWebview() {
        return /* html */ `
			<!DOCTYPE html>
			<html lang="en">
			<body>
				${editorReader.data}
			</body>
			</html>`;
    }
}
exports.editorReader = editorReader;
//# sourceMappingURL=editorReader.js.map