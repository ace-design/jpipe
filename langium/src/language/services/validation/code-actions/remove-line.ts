import { LangiumDocument } from "langium";
import { CodeActionKind, Range, WorkspaceEdit, Diagnostic, TextEdit, integer, CodeActionParams, CodeAction } from "vscode-languageserver";
import { RegistrableCodeAction } from "./code-action-registration.js";
import { LangiumServices } from "langium/lsp";

//Code action to remove a line
export class RemoveLine extends RegistrableCodeAction{
    public title = "Remove line";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(services: LangiumServices, code: string){
        super(services, code);
    }

    protected override getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction> {
        this.diagnostics = [diagnostic];

        this.edit = {
           changes: {
            [document.uri.toString()]: [TextEdit.del(this.getLine(params.range))]
           } 
        }

        this.data = diagnostic.data;
        return [this];
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