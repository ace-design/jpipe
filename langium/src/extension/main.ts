import type { LanguageClientOptions, ServerOptions} from 'vscode-languageclient/node.js';
import type * as vscode from 'vscode';
import {window, commands, Range} from 'vscode';
import * as path from 'node:path';
import { LanguageClient, TransportKind } from 'vscode-languageclient/node.js';

let client: LanguageClient;

//helper function which checks if the cursor is at a certain class type
function cursorAt(class_type: string): boolean{//split into 2 functions to get if it is at the class, then one to set the vscode commands
    const editor = window.activeTextEditor;
    let cursor_at_class = false;
    
    if(editor){
        const document = editor.document;
        let word_range = document.getWordRangeAtPosition(editor.selection.active);
        
        if(word_range){
            let class_correct = isClassCorrect(word_range, document, class_type);
            let diagram_starts = diagramStarts(word_range, document);
            //potentially add a check to see if the function ends (later version)

            if(class_correct && diagram_starts){
                cursor_at_class = true;
            } 
        }
    }

    return cursor_at_class;
}

//helper function to determine if the class type of a selected word is correct
function isClassCorrect(word_range: Range, document: vscode.TextDocument, class_type: string): boolean{
    let class_position = word_range.start.translate(0, -1);
    let class_range = document.getWordRangeAtPosition(class_position);
    let class_name = document.getText(class_range);


    return (class_name === class_type);
}

//helper function for the class type verification process
function diagramStarts(word_range: Range, document: vscode.TextDocument): boolean{
    let function_starts: boolean;

    let new_end = word_range.end.translate(0,2);
    let search_range = new Range(word_range.end, new_end);
    let search_text = document.getText(search_range);
    
    if(search_text.includes('{')){
        function_starts = true;
    }else{
        function_starts = false;
    }

    return function_starts;
}

//function diagramEnds(word_range: Range, document: vscode.TextDocument): boolean{}

// This function is called when the extension is activated.
export function activate(context: vscode.ExtensionContext): void {
    client = startLanguageClient(context);
    
    window.onDidChangeTextEditorSelection(()=>{
        commands.executeCommand('setContext','jpipe.atJustification', cursorAt("justification"));        
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
