import { AstNodeDescription, LangiumDocument, MaybePromise, PrecomputedScopes, URI } from "langium";
import { CodeActionProvider } from "langium/lsp";
import { CodeActionParams, CancellationToken, Command, CodeAction, CodeActionKind, Range, Position, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";


export class JpipeCodeActionProvider implements CodeActionProvider{
    getCodeActions(document: LangiumDocument, params: CodeActionParams, cancelToken?: CancellationToken): MaybePromise<Array<Command | CodeAction> | undefined> {
        if(cancelToken){
            if(cancelToken.isCancellationRequested){
                return undefined;
            }
        }
        
        let code_actions = new Array<CodeAction>();

        params.context.diagnostics.forEach(diagnostic =>{
            if(this.hasCode(diagnostic, "supportInJustification")){
                code_actions.push(
                    new RemoveLine(document.uri, params.range, diagnostic, "supportInJustification")
                );
            }
        })

        return code_actions;
    }

    private hasCode(diagnostic: Diagnostic, code: string): boolean{
        let hasCode = false;

        if(diagnostic.data){
            if(diagnostic.data.code === code){
                hasCode = true;
            }
        }

        return hasCode;
    }

    private findNode(scopes: PrecomputedScopes | undefined, range: Range) {
        let containers = new Map<AstNodeDescription, Range>();
        
        if(scopes){
            scopes.forEach((description, astNode) =>{
                if(description.selectionSegment){
                    if(contains(description.selectionSegment.range, range)){
                        console.log("node found");
                        containers.set(description, description.selectionSegment.range);
                    }
                }
            })
        }

        containers.forEach((description, range) =>{
            
        })
    }
    
}

class RemoveLine implements CodeAction{
    public title = "Remove line";
    public kind = CodeActionKind.QuickFix;
    public edit: WorkspaceEdit;
    public data: string;
    public diagnostics?: Diagnostic[] | undefined;

    constructor(uri: URI, range: Range, diagnostic: Diagnostic, data: string){
        let text_edit = TextEdit.del(this.getLine(range));
        this.edit = {
           changes: {
            [uri.toString()]: [text_edit]
           } 
        }
        this.diagnostics = [diagnostic];
        this.data = data;
    }

    private getLine(range: Range): Range{
        return {
            start: {
                line: range.start.line,
                character: 0
            },
            end: {
                line: range.end.line,
                character: 100000
            }
        };
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

function equals(range1: Range, range2: Range): boolean{
    let equal = false;

    if(range1.start.character === range2.start.character){
        if(range1.start.line === range1.start.line){
            if(range1.end.character === range2.end.character){
                if(range1.end.line === range1.end.line){
                    equal = true;
                }
            }
        }
    }

    return equal;
}

function toString(range: Range): string{
    return "([" + range.start.line + "-" + range.start.character + "], " + "[" + range.end.line + "-" + range.end.character + "])";
}