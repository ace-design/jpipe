import type { LanguageClientOptions, ServerOptions} from 'vscode-languageclient/node.js';
import * as vscode from 'vscode';
import {window} from 'vscode';
import * as path from 'node:path';
import { LanguageClient, TransportKind } from 'vscode-languageclient/node.js';
import { SaveImageCommand } from './image-generation/save-image-command.js';
import { ImageGenerator } from './image-generation/image-generator.js';
import { ContextManager } from './managers/context-manager.js';
import { PreviewProvider } from './image-generation/preview-provider.js';
import { CommandManager } from './managers/command-manager.js';
import { EventManager, EventRunner } from './managers/event-manager.js';
let client: LanguageClient;

// This function is called when the extension is activated.
export function activate(context: vscode.ExtensionContext): void {
    client = startLanguageClient(context);

    //managers for updating and registration
    const command_manager = new CommandManager(context);
    const event_manager = new EventManager();
    const context_manager = new ContextManager(window.activeTextEditor);
    
    //create universal output channel
    const output_channel = vscode.window.createOutputChannel("jpipe_console");

    //create needs for image generation
    const save_image_command = new SaveImageCommand(context, window.activeTextEditor, output_channel);
    const image_generator = new ImageGenerator(save_image_command, output_channel);
    const preview_provider = new PreviewProvider(save_image_command, output_channel);

    //register commands from classes
    command_manager.register(
        image_generator,
        preview_provider
    );

    //register subscribers for events that need to monitor changes
    event_manager.register(new EventRunner(window.onDidChangeTextEditorSelection), context_manager, preview_provider);
    event_manager.register(new EventRunner(window.onDidChangeActiveTextEditor), context_manager, save_image_command, preview_provider);
    event_manager.register(new EventRunner(vscode.workspace.onDidChangeConfiguration), save_image_command);

    vscode.workspace.onDidChangeConfiguration(() =>{
        output_channel.appendLine("configuration changed");
    });

    //activate listening for events
    event_manager.listen();
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