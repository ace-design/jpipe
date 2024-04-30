import { workspace, ExtensionContext, window, ProgressLocation } from 'vscode';
import * as vscode from "vscode";
import * as fs from "fs";
import * as mkdirp from "mkdirp";
import axios from "axios";

import {
    LanguageClient,
    LanguageClientOptions,
    ServerOptions,
} from 'vscode-languageclient/node';

let client: LanguageClient;

export async function activate(context: ExtensionContext) {
    const lspConfig = workspace.getConfiguration("noname-jpipe.language_server", null);
    // let bin_path = lspConfig.get<string | null>("path", null);
    let bin_path = "/Users/nirmalchaudhari/Desktop/lever-jpipe/language-server-jpipe/target/release/jpipe-language-server";

    if (!bin_path) {
        window.showInformationMessage(`No language server path specified.`);
        bin_path = "/Users/nirmalchaudhari/Desktop/lever-jpipe/language-server-jpipe/target/release/jpipe-language-server";
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

export function deactivate(): Thenable<void> | undefined {
    if (!client) {
        return undefined;
    }
    return client.stop();
}
