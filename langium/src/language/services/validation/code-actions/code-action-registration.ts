import { LangiumDocument } from "langium";
import { LangiumServices } from "langium/lsp";
import { CodeAction, CodeActionParams, Diagnostic } from "vscode-languageserver";


export abstract class CodeActionRegistrar{    
    constructor(services: LangiumServices){
        
    }

    public register(code_actions: Array<CodeAction>, document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): void{
        let actions = this.getActions(document, params, diagnostic);

        actions.forEach(action =>{
            code_actions.push(action);
        })
    }

    protected abstract getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction>;
}

export abstract class RegistrableCodeAction extends CodeActionRegistrar implements CodeAction{    
    abstract title: string;
    
    constructor(services: LangiumServices){
       super(services); 
    }
}