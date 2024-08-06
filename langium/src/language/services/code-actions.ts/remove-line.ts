import { URI } from "langium";
import { CodeAction, CodeActionKind, Range, WorkspaceEdit, Diagnostic, TextEdit, integer } from "vscode-languageserver";


export class RemoveLine implements CodeAction{
    public title = "Remove line";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(uri: URI, range: Range, diagnostic: Diagnostic){
        this.diagnostics = [diagnostic];

        this.edit = {
           changes: {
            [uri.toString()]: [TextEdit.del(this.getLine(range))]
           } 
        }

        this.data = diagnostic.data;
    }

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