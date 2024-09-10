import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, Diagnostic, WorkspaceEdit, TextEdit } from "vscode-languageserver";


export class AddConclusion implements CodeAction{
    public title = "Add conclusion";
    public kind = CodeActionKind.QuickFix;

    public diagnostics: Diagnostic[];
    public isPreferred: boolean;

    public edit: WorkspaceEdit;
    public data: any;

    public constructor(document: LangiumDocument, diagnostic: Diagnostic){
        this.diagnostics = [diagnostic];
        this.isPreferred = true;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data;
    }

    private getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit {
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.insert({line: diagnostic.range.start.line+ 1, character: 0}, "\tconclusion c is \"\"\n\n")]
            }
        }
    }
}