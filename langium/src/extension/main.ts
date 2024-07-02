import type { LanguageClientOptions, ServerOptions} from 'vscode-languageclient/node.js';
import type * as vscode from 'vscode';
import {window} from 'vscode';
import * as path from 'node:path';
import { LanguageClient, TransportKind } from 'vscode-languageclient/node.js';
import { SaveImageCommand } from './image-generation/save-image-command.js';
import { ImageGenerator } from './image-generation/image-generator.js';
import { ContextMonitor } from './context-monitor.js';
import { PreviewProvider } from './image-generation/preview-provider.js';
let client: LanguageClient;

// This function is called when the extension is activated.
export function activate(context: vscode.ExtensionContext): void {
    client = startLanguageClient(context);
    
    const context_monitor = new ContextMonitor(window.activeTextEditor);
    
    const save_image_command = new SaveImageCommand(context, window.activeTextEditor);
    let imageGenerator = new ImageGenerator(save_image_command, context);
    imageGenerator.register();
    
    context.subscriptions.push(PreviewProvider.register(context, save_image_command));

    window.onDidChangeTextEditorSelection((changes)=>{
        context_monitor.updateTextSelection(changes);
    });

    window.onDidChangeActiveTextEditor((editor)=>{
        save_image_command.updateEditor(editor);
        context_monitor.updateEditor(editor);
    });
}

// This function is called when the extension is deactivated.
export function deactivate(): Thenable<void> | undefined {
    if (client) {
        return client.stop();
    }
    return undefined;
}

function startLanguageClient(context: vscode.ExtensionContext): LanguageClient {
    const serverModule = context.asAbsolutePath(path.join('out', 'language', 'main.cjs'));
    // The debug options for the server
    // --inspect=6009: runs the server in Node's Inspector mode so VS Code can attach to the server for debugging.
    // By setting `process.env.DEBUG_BREAK` to a truthy value, the language server will wait until a debugger is attached.
    const debugOptions = { execArgv: ['--nolazy', `--inspect${process.env.DEBUG_BREAK ? '-brk' : ''}=${process.env.DEBUG_SOCKET || '6009'}`] };

    // If the extension is launched in debug mode then the debug server options are used
    // Otherwise the run options are used
    const serverOptions: ServerOptions = {
        run: { module: serverModule, transport: TransportKind.ipc },
        debug: { module: serverModule, transport: TransportKind.ipc, options: debugOptions }
    };

    // Options to control the language client
    const clientOptions: LanguageClientOptions = {
        documentSelector: [{ scheme: 'file', language: 'jpipe' }]
    };

    // Create the language client and start the client.
    const client = new LanguageClient(
        'jpipe',
        'jpipe',
        serverOptions,
        clientOptions
    );

    // Start the client. This will also launch the server
    client.start();
    return client;
}