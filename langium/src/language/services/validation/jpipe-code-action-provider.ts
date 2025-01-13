import { LangiumDocument, MaybePromise} from "langium";
import { CodeActionProvider, LangiumServices } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, Diagnostic } from "vscode-languageserver";
import { RemoveLine, RemoveImplemented, ChangeDeclarationRegistrar, ResolveReferenceRegistrar } from "./code-actions/index.js";
import { AddConclusion } from "./code-actions/add-conclustion.js";
import { CodeActionRegistrar } from "./code-actions/code-action-registration.js";


//class which provides all code actions (quick fixes)
export class JpipeCodeActionProvider implements CodeActionProvider{
    private diagnostic_registrars: Map<string, Array<CodeActionRegistrar>>;
    
    constructor(readonly services: LangiumServices){
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

        this.register("linking-error", [
            new ResolveReferenceRegistrar(this.services, "linking-error")
        ])

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

