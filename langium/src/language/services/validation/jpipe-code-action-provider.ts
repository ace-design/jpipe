import { IndexManager, LangiumDocument, LinkingErrorData, MaybePromise, URI} from "langium";
import { CodeActionProvider, LangiumServices } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, Diagnostic } from "vscode-languageserver";
import { RemoveLine, ChangeDeclarationKind, ResolveReference, RemoveImplemented, ChangeDeclarationRegistrar, ResolveReferenceRegistrar } from "./code-actions/index.js";
import { AstUtils } from "langium";
import { AddConclusion } from "./code-actions/add-conclustion.js";
import { CodeActionRegistrar } from "./code-actions/code-action-registration.js";


//class which provides all code actions (quick fixes)
export class JpipeCodeActionProvider implements CodeActionProvider{
    private readonly index_manager: IndexManager;
    private diagnostic_registrars: Map<string, Array<CodeActionRegistrar>>;
    
    constructor(readonly services: LangiumServices){
        this.index_manager = services.shared.workspace.IndexManager;
        this.diagnostic_registrars = new Map();

        this.register("supportInJustification", [
            new ChangeDeclarationRegistrar(this.services, "supportInJustification"), 
            new RemoveLine(this.services, "supportInJustification")
        ])

        this.register("noSupportInPattern",[
            new ChangeDeclarationRegistrar(this.services, "noSupportInPattern")
        ])
        
        this.register("supportNotMatching", [
            new RemoveLine(services, "supportNotMatching")
        ])

        this.register("compositionImplementing", [
            new ChangeDeclarationRegistrar(this.services, "compositionImplementing"),
            new RemoveImplemented(this.services, "compositionImplementing")     
        ])

        this.register("nonPatternImplemented", [
            new RemoveImplemented(this.services, "nonPatternImplementing")
        ])

        this.register("noConclusionInJustification", [
            new AddConclusion(this.services, "noConclusionInJustification"),
            new ChangeDeclarationRegistrar(this.services, "noConclusionInJustification")
        ]);

        // this.register("linking-error", [
        //     new ResolveReferenceRegistrar(this.services, "linking-error")
        // ])
    }

    public register(diagnostic_data_code: string, registrars: Array<CodeActionRegistrar>): void{
        this.diagnostic_registrars.set(diagnostic_data_code, registrars);
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
            let action_registrars: Array<CodeActionRegistrar> = [];

            if(code){
                let registrars = this.diagnostic_registrars.get(code);

                if(registrars){
                    registrars.forEach( registrar => {
                        action_registrars.push(registrar)
                    })
                }
            }

            action_registrars.forEach(registrar => {
                registrar.register(code_actions, document, params, diagnostic);
            })
        })

        // params.context.diagnostics.forEach(diagnostic => {
        //     let code = this.getCode(diagnostic);

        //     if (code === "supportInJustification") {
        //         code_actions.push(
        //             new ChangeDeclarationKind(document, params, diagnostic),
        //             new RemoveLine(document, params, diagnostic)
        //         );

        //     }else if (code === "noSupportInPattern") {
        //         code_actions.push(
        //             new ChangeDeclarationKind(document, params, diagnostic)
        //         );

        //     }else if (code === "supportNotMatching") {
        //         code_actions.push(
        //             new RemoveLine(document, params, diagnostic)
        //         );
        //     }else }else if (code === "compositionImplementing") {
        //         code_actions.push(
        //             new ChangeDeclarationKind(document, params, diagnostic, "pattern"),
        //             new ChangeDeclarationKind(document, params, diagnostic, "justification"),
        //             new RemoveImplemented(document, diagnostic)
        //         )
        //     }else if (code === "nonPatternImplemented") {
        //         code_actions.push(
        //             new RemoveImplemented(document, diagnostic)
        //         )
        //     }else if(code === "noConclusionInJustification"){
        //         code_actions.push(                    
        //             new AddConclusion(document, diagnostic),
        //             new ChangeDeclarationKind(document, params, diagnostic, "pattern")
        //         )
        //     }

        // })

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
                    let home_doc = AstUtils.getDocument(e.node);

                    paths.add(home_doc.uri);
                }
            }
        });

        return paths;
    }
}

