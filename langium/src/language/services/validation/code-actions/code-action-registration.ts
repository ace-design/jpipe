import { LangiumDocument } from "langium";
import { LangiumServices } from "langium/lsp";
import { CodeAction, CodeActionParams, Diagnostic } from "vscode-languageserver";






export abstract class CodeActionRegistrar{
    constructor(services: LangiumServices, code: string){
        
    }

    public register(code_actions: Array<CodeAction>, document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): void{
        let actions = this.getActions(document, params, diagnostic);

        actions.forEach(action =>{
            code_actions.push(action);
        })
    }

    protected abstract getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction>;
}

//For any code action which can register itself without additional help from a factory
export abstract class RegistrableCodeAction extends CodeActionRegistrar implements CodeAction{    
    abstract title: string;
    
    constructor(services: LangiumServices, code: string){
       super(services, code); 
    }

    protected getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction>{
        return [this.getAction(document, params, diagnostic)];
    };

    protected abstract getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction;
}