import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, CodeActionParams, Diagnostic, TextEdit, WorkspaceEdit } from "vscode-languageserver";


export class RemoveImplemented implements CodeAction{
    public title = "Remove implementation";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;
   
    
    public constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic){
        this.diagnostics = [diagnostic];
        this.isPreferred = false;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data; 
    }

    private getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit{
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.del(diagnostic.range)]
            }
        }
    }
}