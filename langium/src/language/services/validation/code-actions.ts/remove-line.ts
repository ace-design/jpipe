import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, Range, WorkspaceEdit, Diagnostic, TextEdit, integer, CodeActionParams } from "vscode-languageserver";

//Code action to remove a line
export class RemoveLine implements CodeAction{
    public title = "Remove line";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic){
        this.diagnostics = [diagnostic];

        this.edit = {
           changes: {
            [document.uri.toString()]: [TextEdit.del(this.getLine(params.range))]
           } 
        }

        this.data = diagnostic.data;
    }

    //helper function to get the line which contains the range
    private getLine(range: Range): Range{
        return {
            start: {
                line: range.start.line,
                character: 0
            },
            end: {
                line: range.end.line,
                character: integer.MAX_VALUE
            }
        };
    }
}