import { LangiumDocument, MaybePromise, MultiMap} from "langium";
import { CodeActionProvider, LangiumServices } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, Diagnostic } from "vscode-languageserver";
import { RemoveLine, RemoveImplemented, ChangeDeclarationRegistrar, ResolveReferenceRegistrar, AddVariableDefinitionLine, CodeActionRegistrar, VariableType } from "./code-actions/index.js";

//class which provides all code actions (quick fixes)
export class JpipeCodeActionProvider implements CodeActionProvider{
    private diagnostic_registrars: MultiMap<string, CodeActionRegistrar>;
    
    constructor(readonly services: LangiumServices){
        this.diagnostic_registrars = new MultiMap<string, CodeActionRegistrar>();

        //array of all registrars and their associated error codes
        const registrars: Array<CodeActionRegistrar> = [
            new ChangeDeclarationRegistrar("supportInJustification"), 
            new ChangeDeclarationRegistrar("noSupportInPattern"),
            new ChangeDeclarationRegistrar("compositionImplementing"),
            new ChangeDeclarationRegistrar("noConclusionInJustification"),
            new RemoveLine("supportInJustification"),
            new RemoveLine("supportNotMatching"),
            new RemoveImplemented("compositionImplementing"),
            new RemoveImplemented("nonPatternImplementing"),
            new AddVariableDefinitionLine("noConclusionInJustification", VariableType.CONCLUSION), //while both add conclusions do the same thing, i kept the error codes separate in case I wanted to change the implementation in the future
            new AddVariableDefinitionLine("noConclusionInPattern", VariableType.CONCLUSION),
            new AddVariableDefinitionLine("noSupportInPattern", VariableType.SUPPORT),
            new ResolveReferenceRegistrar(this.services, "linking-error")
        ]

        registrars.forEach(registrar => {
            this.diagnostic_registrars.add(registrar.code, registrar)
        })
    }

    //returns codes based on diagnostics
    public getCodeActions(document: LangiumDocument, params: CodeActionParams, cancelToken?: CancellationToken): MaybePromise<Array<Command | CodeAction> | undefined> {
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

