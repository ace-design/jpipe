import { AstNode, AstNodeDescription, LangiumDocument, PrecomputedScopes, TextDocument } from "langium";
import { CodeActionParams, CodeAction, CodeActionKind, Range, Position, WorkspaceEdit, Diagnostic, TextEdit, integer } from "vscode-languageserver";
import { getDeclaration, getModelNode } from "../../jpipe-scope-provider.js";
import { Declaration } from "../../../generated/ast.js";
import { contains } from "./range-utilities.js";

//custom type to store map entries
type MapEntry<K,T> = {
    key?: K
    value: T
}

//Code action to change the declaration
export class ChangeDeclaration implements CodeAction{
    public title!: string;
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    //automatically detects and changes pattern => justification, justification -> pattern, and composition -> composition
    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic);
    //set what you want the declaration to be changed to
    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic, change: string);
    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic, change?: string){
        let node = this.findNode(document.precomputedScopes, params.range);  

        if(node.node){
            let declaration = getDeclaration(node.node);
            
            if(change === undefined){
                change = this.getChangeType(declaration.kind);
            }

            this.title = "Change declaration type to " + change;

            this.diagnostics = [diagnostic];
            this.isPreferred = true;
            this.data = diagnostic.data;
            this.edit = this.getEdit(declaration, change, document);
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

    //helper function to automatically determine what the declaration kind should change to
    private getChangeType(declaration_kind: 'composition' | 'justification' | 'pattern'): 'composition' | 'justification' | 'pattern'{
        let change_to: 'composition' | 'justification' | 'pattern';

        if(declaration_kind === "justification"){
            change_to =  "pattern";
        }else if(declaration_kind === "pattern"){
            change_to = "justification";
        }else{
            change_to = "composition";
        }

        return change_to;        
    }

    //helper function to get the range of a text element given a search range and a document
    private getRange(document: TextDocument, search_range: Range, kind: string): Range {
        let check_start = search_range.start;
        let check_end: Position = {character: check_start.character + kind.length, line: check_start.line};

        let check_range = Range.create(check_start, check_end);
        let check_text = document.getText(check_range);
        
        while(check_text !== "" && check_text !== kind){
            check_start = {character: check_start.character + 1, line: check_start.line};
            check_end = {character: check_start.character + kind.length, line: check_start.line};
            
            check_range = Range.create(check_start, check_end);
            check_text = document.getText(check_range);
        }

        if(check_text){
            return check_range;
        }else{
            throw new Error("text not found");
        }
    }

    //helper function to find the smallest node which contains a given range
    private findNode(scopes: PrecomputedScopes | undefined, range: Range): AstNodeDescription {        
        let smallest_container: MapEntry<AstNodeDescription, Range> = {
            value: {start: {line: 0, character: 0}, end: {line: integer.MAX_VALUE, character: integer.MAX_VALUE} },
        }

        scopes?.forEach((description) =>{
            if(description.selectionSegment){
                if(contains(description.selectionSegment.range, range)){
                    smallest_container = this.getSmallestContainer(smallest_container, {key: description, value: description.selectionSegment.range});
                }
            }
        });

        if(smallest_container.key){
            return smallest_container.key;
        }else{
            throw new Error("Smallest container cannot be found");
        }

    }

    //helper function to determine which container is the smallest between a current and a new range
    private getSmallestContainer(current_smallest: MapEntry<AstNodeDescription, Range>, new_comparison: MapEntry<AstNodeDescription, Range>): MapEntry<AstNodeDescription, Range>{
        let new_smallest: MapEntry<AstNodeDescription, Range>;
        let smaller_than_smallest = contains(current_smallest.value, new_comparison.value);

        if(smaller_than_smallest){
            new_smallest = new_comparison;
        }else{
            new_smallest = current_smallest;
        }

        return new_smallest;
    }
}