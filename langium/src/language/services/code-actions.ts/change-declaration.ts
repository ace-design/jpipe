import { AstNode, AstNodeDescription, LangiumDocument, PrecomputedScopes, TextDocument } from "langium";
import { CodeActionParams, CodeAction, CodeActionKind, Range, Position, WorkspaceEdit, Diagnostic, TextEdit, integer } from "vscode-languageserver";
import { getDeclaration, getModelNode } from "../jpipe-scope-provider.js";
import { Class } from "../../generated/ast.js";

type MapEntry<K,T> = {
    key?: K
    value: T
}

export class ChangeDeclaration implements CodeAction{
    public title!: string;
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic){
        let node = this.findNode(document.precomputedScopes, params.range);
                

        if(node.node){
            let declaration = getDeclaration(node.node);
            let change_to = this.getChangeType(declaration.kind);

            this.title = "Change declaration type to " + change_to;

            this.diagnostics = [diagnostic];
            this.isPreferred = true;
            this.data = diagnostic.data;
            this.edit = this.getEdit(declaration, change_to, document);
        }
    
    }

    private getEdit(declaration: Class, change_to: string, document: LangiumDocument): WorkspaceEdit{
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

    private getRange(document: TextDocument, search_range: Range, kind: string): Range {
        let check_start = search_range.start;
        let check_end: Position = {character: check_start.character + kind.length, line: check_start.line};
        let check_range = Range.create(check_start, check_end);
        let check_text = document.getText(check_range);
        
        while(check_text !== "" && check_text !== kind){
            console.log(check_text)
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

function contains(container: Range, contained: Range): boolean{
    let contains = false;

    if(compare(container.start, contained.start) <= 0){
        if(compare(container.end, contained.end)>=0){
            contains = true;
        }
    }

    return contains;
}

//returns 1 if p1>p2, returns -1 if p2<p1, returns 0 if p1=p2
function compare(p1: Position, p2: Position): number{
    if(p1.line > p2.line){
        return 1;
    }else if(p1.line < p2.line){
        return -1;
    }else{
        if(p1.character > p2.character){
            return 1;
        }else if(p1.character < p2.character){
            return -1;
        }else{
            return 0;
        }
    }
}