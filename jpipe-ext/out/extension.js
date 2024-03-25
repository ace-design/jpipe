"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
const editorReader_1 = require("./editorReader");
const vscode_1 = require("vscode");
const node_1 = require("vscode-languageclient/node");
let client;
// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
async function activate(context) {
    context.subscriptions.push(editorReader_1.editorReader.register(context));
    const lspConfig = vscode_1.workspace.getConfiguration("noname-jpipe.language_server", null);
    let bin_path = lspConfig.get("path", null);
    if (!bin_path) {
        vscode_1.window.showInformationMessage(`No language server path specified.`);
        bin_path = "../jpipe-language-server";
        await lspConfig.update("path", bin_path, true);
    }
    const serverOptions = {
        command: bin_path,
    };
    // Options to control the language client
    const clientOptions = {
        // Register the server for plain text documents
        documentSelector: [{ scheme: 'file', language: 'jpipe' }],
        synchronize: {
            // Notify the server about file changes to '.clientrc files contained in the workspace
            fileEvents: vscode_1.workspace.createFileSystemWatcher('**/.clientrc')
        }
    };
    // Create the language client and start the client.
    client = new node_1.LanguageClient('language-server-jpipe', 'jpipe Language Server', serverOptions, clientOptions);
    // Start the client. This will also launch the server
    return client.start().catch(reason => {
        vscode_1.window.showWarningMessage(`Failed to run language server: ${reason}`);
        client = null;
    });
}
exports.activate = activate;
// This method is called when your extension is deactivated
function deactivate() {
    if (!client) {
        return undefined;
    }
    return client.stop();
}
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map