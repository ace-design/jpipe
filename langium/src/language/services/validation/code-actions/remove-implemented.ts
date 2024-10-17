import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, CodeActionParams, Diagnostic, TextEdit, WorkspaceEdit } from "vscode-languageserver";
import { RegistrableCodeAction } from "./code-action-registration.js";
import { LangiumServices } from "langium/lsp";


export class RemoveImplemented extends RegistrableCodeAction{
    public title = "Remove implementation";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(services: LangiumServices, code: string){
        super(services, code);
    }

    protected override getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction {
        this.diagnostics = [diagnostic];
        this.isPreferred = false;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data; 
        
        return this;
    }

    private getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit{
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.del(diagnostic.range)]
            }
        }
    }
}