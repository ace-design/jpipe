// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import { editorReader } from './editorReader';

import { workspace, ExtensionContext, window, ProgressLocation } from 'vscode';
import * as fs from "fs";

import {
    LanguageClient,
    LanguageClientOptions,
    ServerOptions,
} from 'vscode-languageclient/node';

let client: LanguageClient | null;


// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export async function activate(context: vscode.ExtensionContext) {
	context.subscriptions.push(editorReader.register(context));

	const lspConfig = workspace.getConfiguration("noname-jpipe.language_server", null);
    let bin_path = lspConfig.get<string | null>("path", null);

    if (!bin_path) {
        window.showInformationMessage(`No language server path specified.`);
        bin_path = "../jpipe-language-server";
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

    // Start the client. This will also launch the server
    return client.start().catch(reason => {
        window.showWarningMessage(`Failed to run language server: ${reason}`);
        client = null;
    });
}

// This method is called when your extension is deactivated
export function deactivate() {
	if (!client) {
        return undefined;
    }
    return client.stop();
}
