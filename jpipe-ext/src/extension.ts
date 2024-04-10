import * as vscode from 'vscode';
import { editorReader } from './editorReader';

import { workspace, ExtensionContext, window, ProgressLocation } from 'vscode';

import {
    LanguageClient,
    LanguageClientOptions,
    ServerOptions,
} from 'vscode-languageclient/node';

let client: LanguageClient | null;


export async function activate(context: vscode.ExtensionContext) {


    // Get the lsp configuration from the package.json file.
	const lspConfig = workspace.getConfiguration("noname-jpipe.language_server", null);

    // Determine what the bin path is. Since lsp binary is packaged with the extension, it is in the main directory of the extension.
    let bin_path = context.extensionUri.path+"/jpipe-language-server";

    if (!bin_path) {
        window.showInformationMessage(`No language server path specified.`);
        bin_path = context.extensionUri.path+"/jpipe-language-server";
        await lspConfig.update("path", bin_path, true);
    }

    const serverOptions: ServerOptions = {
        command: bin_path,
    };

    // Options to control the language client
    const clientOptions: LanguageClientOptions = {
        // Register the server for plain text documents
        documentSelector: [{ scheme: 'file', language: 'jpipe' }],
        synchronize: {
            // Notify the server about file changes to '.clientrc files contained in the workspace
            fileEvents: workspace.createFileSystemWatcher('**/.clientrc')
        }
    };

    // Create the language client and start the client.
    client = new LanguageClient(
        'language-server-jpipe',
        'jpipe Language Server',
        serverOptions,
        clientOptions
    );

    context.subscriptions.push(editorReader.register(context));


    // Start the client. This will also launch the server
    return client.start().catch(reason => {
        window.showWarningMessage(`Failed to run language server: ${reason}`);
        client = null;
    });
}

// This method is called when extension is deactivated
export function deactivate() {
	if (!client) {
        return undefined;
    }
    return client.stop();
}
