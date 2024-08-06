import { LangiumDocument, MaybePromise } from "langium";
import { CodeActionProvider } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, Diagnostic } from "vscode-languageserver";
import { ChangeDeclaration } from "./code-actions.ts/change-declaration.js";
import { RemoveLine } from "./code-actions.ts/remove-line.js";

//class which provides all code actions (quick fixes)
export class JpipeCodeActionProvider implements CodeActionProvider{
    //returns codes based on diagnostics
    getCodeActions(document: LangiumDocument, params: CodeActionParams, cancelToken?: CancellationToken): MaybePromise<Array<Command | CodeAction> | undefined> {
        if(cancelToken){
            if(cancelToken.isCancellationRequested){
                return undefined;
            }
        }
        
        let code_actions = new Array<CodeAction>();

        params.context.diagnostics.forEach(diagnostic => {
            let code = this.getCode(diagnostic);

            switch (code) {
                case "supportInJustification":
                    code_actions.push(
                        new ChangeDeclaration(document, params, diagnostic),
                        new RemoveLine(document, params, diagnostic)
                    );
                    break;
                case "noSupportInPattern":
                    code_actions.push(
                        new ChangeDeclaration(document, params, diagnostic)
                    );
                    break;
                case "supportNotMatching":
                    code_actions.push(
                        new RemoveLine(document, params, diagnostic)
                    );
                    break;
                case "linking-error":
                    
                    break;
                default:
                    break;
            }
        })

        return code_actions;
    }

    //helper function to get the code of a diagnostic
    private getCode(diagnostic: Diagnostic): string | undefined{
        let code: string | undefined;

        if(diagnostic.data){
            if(diagnostic.data.code){
                code = diagnostic.data.code
            }
        }

        return code;
    }
}

