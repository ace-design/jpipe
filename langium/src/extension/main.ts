import type { LanguageClientOptions, ServerOptions} from 'vscode-languageclient/node.js';
import * as vscode from 'vscode';
import * as path from 'node:path';
import { LanguageClient, TransportKind } from 'vscode-languageclient/node.js';
import { CommandManager, ConfigurationManager, ContextManager, EnvironmentCheckManager, EventManager, EventRunner } from './managers/index.js';
import { ImageGenerator, PreviewProvider } from './image-generation/index.js';

let client: LanguageClient;

// This function is called when the extension is activated.
export function activate(context: vscode.ExtensionContext): void {
    client = startLanguageClient(context);
    //create universal output channel
    const output_channel = vscode.window.createOutputChannel("jpipe_console");

    //managers for updating and registration
    const command_manager = new CommandManager(context);
    const event_manager = new EventManager();
    const context_manager = new ContextManager(vscode.window.activeTextEditor);
    const configuration_manager = new ConfigurationManager(context, output_channel);
    const environment_manager = new EnvironmentCheckManager(configuration_manager);
    

    //create needs for image generation
    const image_generator = new ImageGenerator(configuration_manager, output_channel);
    const preview_provider = new PreviewProvider(image_generator, output_channel);

    //register commands from classes
    command_manager.register(
        image_generator,
        preview_provider
    );

    //register subscribers for events that need to monitor changes
    event_manager.register(new EventRunner(vscode.window.onDidChangeTextEditorSelection), context_manager, preview_provider);
    event_manager.register(new EventRunner(vscode.window.onDidChangeActiveTextEditor), context_manager, image_generator, preview_provider);
    event_manager.register(new EventRunner(vscode.workspace.onDidChangeConfiguration), configuration_manager);

    //activate listening for events
    event_manager.listen();  

    //check the environment for needed installations
    environment_manager.run()
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