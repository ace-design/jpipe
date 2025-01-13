import { LangiumDocument } from "langium";
import { CodeAction, CodeActionParams, Diagnostic } from "vscode-languageserver";

//abstract class for a code actino registrar (factory to create code actions associated with certain codes)
export abstract class CodeActionRegistrar{
    public readonly code: string;
    constructor(code: string){
        this.code = code;
    }

    //registration function to add code action to the given array based on diagnostic information
    public register(code_actions: Array<CodeAction>, document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): void{
        let actions = this.getActions(document, params, diagnostic);

        actions.forEach(action =>{
            code_actions.push(action);
        })
    }

    //returns the actions associated to this registrar given the diagnostic information
    protected abstract getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction>;
}

//For any code action which can register itself without additional help from a factory
export abstract class RegistrableCodeAction extends CodeActionRegistrar implements CodeAction{    
    abstract title: string;
    
    constructor(code: string){
       super(code); 
    }

    //returns itself
    protected getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction>{
        return [this.getAction(document, params, diagnostic)];
    };

    //function to create itself
    protected abstract getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction;
}