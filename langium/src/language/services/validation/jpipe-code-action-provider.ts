import { IndexManager, LangiumDocument, LinkingErrorData, MaybePromise, URI } from "langium";
import { CodeActionProvider, LangiumServices } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, Diagnostic } from "vscode-languageserver";
import { RemoveLine, ChangeDeclarationKind, ResolveReference } from "./code-actions/index.js";
import { getDocument } from "../../../../node_modules/langium/lib/utils/ast-utils.js";

//class which provides all code actions (quick fixes)
export class JpipeCodeActionProvider implements CodeActionProvider{
    private readonly index_manager: IndexManager;

    constructor(services: LangiumServices){
        this.index_manager = services.shared.workspace.IndexManager;
    }
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
                        new ChangeDeclarationKind(document, params, diagnostic),
                        new RemoveLine(document, params, diagnostic)
                    );
                    break;
                case "noSupportInPattern":
                    code_actions.push(
                        new ChangeDeclarationKind(document, params, diagnostic)
                    );
                    break;
                case "supportNotMatching":
                    code_actions.push(
                        new RemoveLine(document, params, diagnostic)
                    );
                    break;
                case "linking-error":
                    let data = this.toLinkingError(diagnostic.data);
                    let paths = this.getPaths(document, data);

                    paths.forEach(path =>{
                        code_actions.push(new ResolveReference(document, diagnostic, path))
                    });
                    break;
                case "nonJustificationImplementing":
                    //To Do: Add actions
                    break;
                case "nonPatternImplemented":
                    //To Do: Add actions
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

    private toLinkingError(data: any): LinkingErrorData{
        if(data.code && data.containerType && data.property && data.refText){
            return {
                code: data.code,
                containerType: data.containerType,
                property: data.property,
                refText: data.refText,
                actionSegment: data.actionSegment,
                actionRange: data.actionRange
            }
        }else{
            throw new Error("Diagnostic data not in correct form");
        }
    }

    private getPaths(document: LangiumDocument, data: LinkingErrorData): Set<URI>{
        let paths = new Set<URI>();

        this.index_manager.allElements(data.containerType).forEach(e=>{
            if(e.name === data.refText){
                if(e.node){
                    let home_doc = getDocument(e.node);

                    paths.add(home_doc.uri);
                }
            }
        });

        return paths;
    }
}

