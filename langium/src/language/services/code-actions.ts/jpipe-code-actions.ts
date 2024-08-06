import { LangiumDocument, MaybePromise, URI } from "langium";
import { CodeActionProvider } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, CodeActionKind, Range, Position, WorkspaceEdit, Diagnostic, TextEdit, integer } from "vscode-languageserver";
import { ChangeDeclaration } from "./change-declaration.js";
import { RemoveLine } from "./remove-line.js";

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
                    new ChangeDeclaration(document, params, diagnostic),
                    new RemoveLine(document.uri, params.range, diagnostic)
                );
            }else if(this.hasCode(diagnostic, "noSupportInPattern")){
                code_actions.push(
                    new ChangeDeclaration(document, params, diagnostic)
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

function equals(range1: Range, range2: Range): boolean{
    let equal = false;

    if(range1.start.character === range2.start.character){
        if(range1.start.line === range1.start.line){
            if(range1.end.character === range2.end.character){
                if(range1.end.line === range1.end.line){
                    equal = true;
                }
            }
        }
    }

    return equal;
}

function toString(range: Range | undefined): string{
    if(!range){
        return "";
    }
    return "([" + range.start.line + "-" + range.start.character + "], " + "[" + range.end.line + "-" + range.end.character + "])";
}