import { AstNode, AstNodeDescription, LangiumDocument, TextDocument } from "langium";
import { CodeActionParams, CodeAction, CodeActionKind, Range, Position, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";
import { getDeclaration, getModelNode } from "../../jpipe-scope-provider.js";
import { Declaration } from "../../../generated/ast.js";
import { getAnyNode as getNode } from "./utilities/node-utilities.js";
import { makePosition } from "./utilities/range-utilities.js";
import { CodeActionRegistrar } from "./code-action-registration.js";

//class which registers change declaration code actions
export class ChangeDeclarationRegistrar extends CodeActionRegistrar{
    protected override getActions: (document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => Array<CodeAction>;

    private codes: Map<string, Array<(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) =>CodeAction>> = this.registerActions();;

    constructor(code: string){
        super(code);

        let actions: ((document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => CodeAction)[] | undefined = this.codes.get(code);

        this.getActions = actions ? this.unpackActions(actions) : this.emptyActions;
    }
    
    //helper function to register code actions to their associated codes
    private registerActions(): Map<string, Array<(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) =>CodeAction>>{
        let map = new Map<string, Array<(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => CodeAction>>();

        map.set("supportInJustification", [this.pattern]);
        map.set("noSupportInPattern", [this.justification]);
        map.set("compositionImplementing", [this.pattern, this.justification]);

        return map;
    } 

    //helper function to create the justification change declaration kind
    private justification(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): ChangeDeclarationKind{
        return new ChangeDeclarationKind(document,params, diagnostic, "justification");
    }

    //helper function to create the pattern change declaration kind
    private pattern(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): ChangeDeclarationKind{
        return new ChangeDeclarationKind(document,params, diagnostic, "pattern");
    }

    //helper function to create the composition change declaraion kind
    // private composition(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): ChangeDeclarationKind{
    //     return new ChangeDeclarationKind(document,params, diagnostic, "composition");
    // }

    //returns a function to create the change declaration array
    private unpackActions(actions: ((document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => CodeAction)[]): (document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => Array<CodeAction>{
        return (document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic) => {
            let code_action_array = new Array<CodeAction>();
            
            actions.forEach(action =>
                {
                code_action_array.push(action(document, params,diagnostic));
                }
            )
            
            return code_action_array;
        }
    }

    //helper function to create an empty action array
    private emptyActions = () =>{
        return [];
    }
}

//Code action to change the declaration
export class ChangeDeclarationKind implements CodeAction{
    public title!: string;
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    //set what you want the declaration to be changed to
    public constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic, change: string){
        let node = getNode(params.range, document.precomputedScopes);  

        if(node.node){
            let declaration_name = getDeclaration(node.node);

            this.title = "Change declaration type to " + change;

            this.diagnostics = [diagnostic];
            this.isPreferred = true;
            this.data = diagnostic.data;
            this.edit = this.getEdit(declaration_name, change, document);
        }
    
    }

    //helper function to make the text edit
    private getEdit(declaration: Declaration, change_to: string, document: LangiumDocument): WorkspaceEdit{
        let edit: WorkspaceEdit | undefined;
        
        let model = getModelNode(declaration);
        
        if(model){
            let description = this.getDescription(document, model, declaration.name);

            if(description.selectionSegment && model.$document){
                let range = this.getRange(document.textDocument, description.selectionSegment.range, declaration.kind);
                edit = {
                    changes: {
                        [model.$document.uri.toString()]: [TextEdit.replace(range, change_to)]
                    } 
                }
            }
        }

        if(edit){
            return edit;
        }else{
            throw new Error("Workspace edit cannot be made");
        }
    }

    //helper function to get the AstNodeDescription for a given class based on the declaration name
    private getDescription(document: LangiumDocument, model: AstNode, declaration_name: string): AstNodeDescription{
        let final_description: AstNodeDescription | undefined;

        let descriptions = document.precomputedScopes?.get(model);

        descriptions?.forEach((description) =>{
            if(description.name === declaration_name){
                final_description = description;
            }
        })

        if(final_description){
            return final_description;
        }else{
            throw new Error("Final description is undefined");
        }
    }

    //helper function to get the range of a text element given a search range and a document
    private getRange(document: TextDocument, search_range: Range, kind: string): Range {
        let check_start = search_range.start;

        let check_end: Position = makePosition(check_start.line, check_start.character + kind.length);

        let check_range = Range.create(check_start, check_end);
        let check_text = document.getText(check_range);
        
        while(check_text !== "" && check_text !== kind){
            check_start = makePosition(check_start.line, check_start.character +1);
            check_end = makePosition(check_start.line, check_start.character + kind.length);
            
            check_range = Range.create(check_start, check_end);
            check_text = document.getText(check_range);
        }

        if(check_text){
            return check_range;
        }else{
            throw new Error("text not found");
        }
    }
}