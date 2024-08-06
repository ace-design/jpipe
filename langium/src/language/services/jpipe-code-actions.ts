import { LangiumDocument, MaybePromise, URI } from "langium";
import { CodeActionProvider } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, CodeActionKind, Range, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";


export class JpipeCodeActionProvider implements CodeActionProvider{
    getCodeActions(document: LangiumDocument, params: CodeActionParams, cancelToken?: CancellationToken): MaybePromise<Array<Command | CodeAction> | undefined> {
        if(cancelToken){
            if(cancelToken.isCancellationRequested){
                return undefined;
            }
        }

        let code_actions = new Array<CodeAction>();

        params.context.diagnostics.forEach(diagnostic =>{
            if(this.hasCode(diagnostic, "supportInJustification")){
                code_actions.push(
                    new RemoveLine(document.uri, params.range, diagnostic, "supportInJustification")
                );
            }
        })

        return code_actions;
    }

    private hasCode(diagnostic: Diagnostic, code: string): boolean{
        let hasCode = false;

        if(diagnostic.data){
            if(diagnostic.data.code === code){
                hasCode = true;
            }
        }

        return hasCode;
    }
    
}

class RemoveLine implements CodeAction{
    public title = "Remove line";
    public kind = CodeActionKind.QuickFix;
    public edit: WorkspaceEdit;
    public data: string;
    public diagnostics?: Diagnostic[] | undefined;

    constructor(uri: URI, range: Range, diagnostic: Diagnostic, data: string){
        let text_edit = TextEdit.del(this.getLine(range));
        this.edit = {
           changes: {
            [uri.toString()]: [text_edit]
           } 
        }
        this.diagnostics = [diagnostic];
        this.data = data;
    }

    private getLine(range: Range): Range{
        return {
            start: {
                line: range.start.line,
                character: 0
            },
            end: {
                line: range.end.line,
                character: 100000
            }
        };
    }
}