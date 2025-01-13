import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, CodeActionParams, Diagnostic, TextEdit, WorkspaceEdit } from "vscode-languageserver";
import { RegistrableCodeAction } from "./code-action-registration.js";

//removes an implemented diagram
export class RemoveImplemented extends RegistrableCodeAction{
    public title = "Remove implementation";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(code: string){
        super(code);
    }

    //returns the action to remove the implemented pattern/justification
    protected override getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction {
        this.diagnostics = [diagnostic];
        this.isPreferred = false;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data; 
        
        return this;
    }

    //helper function to create the edit associated with removing the implemented pattern/justification
    private getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit{
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.del(diagnostic.range)]
            }
        }
    }
}