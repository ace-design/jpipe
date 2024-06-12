import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isSupport, isVariable } from "../generated/ast.js";
import { stream } from "../../../node_modules/langium/src/utils/stream.js"

export class JpipeCompletionProvider extends DefaultCompletionProvider{
    //track which variable types can support other variable types
    readonly typeMap: Map<string, string[]> = new Map<string,string[]>([
        ['evidence', ['strategy']],
        ['strategy', ['sub-conclusion', 'conclusion']],
        ['sub-conclusion', ['strategy', 'conclusion']],
        ['conclusion', []] 
    ]);

    //filters reference candidates for variables in support statements for autocompletion
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        let potential_references = this.scopeProvider.getScope(refInfo).getAllElements();
        //let references: AstNodeDescription[] = [];
        //let variables: AstNodeDescription[] = [];
        let references = this.findKeywords(potential_references);
        let variables = this.findVariables(potential_references);

        //tracking which are variables vs keywords
        /*
        potential_references.forEach((potential) =>{
            if(isVariable(potential.node)){
                variables.push(potential);
            }else{
                references.push(potential);
            }
        });
        */

        //if the current context is of a supporting statement, determines which variables should appear for autocomplete
        if(isSupport(_context.node)){
            if(_context.node.left.ref !== undefined){
                this.getRightVariables(variables,_context).forEach((v)=>{
                    references.push(v);
                });
            }else{
                this.getLeftVariables(variables,_context).forEach((v)=>{
                    references.push(v);
                });
            }
        }
        return stream(references);
    }

    //helper function for filtering references
    findVariables(potential_references:Stream<AstNodeDescription>): AstNodeDescription[]{
        let variables: AstNodeDescription[]=[]
        potential_references.forEach((potential) =>{
            if(isVariable(potential.node)){
                variables.push(potential);
            }
        });
        return variables;
    }

    //helper function for finding non-variable keywords
    findKeywords(potential_references:Stream<AstNodeDescription>): AstNodeDescription[]{
        let keywords: AstNodeDescription[] = [];

        potential_references.forEach(potential =>{
            if(!isVariable(potential.node)){
                keywords.push(potential);
            }
        });

        return keywords;
    }

    
    //autocompletes right-side variables so that only those which fit the format are shown
    //ex. if your JD defines evidence 'e1', strategy 'e2' and conclusion e3', when starting the statement:
    //e2 supports ___ auto-completion will only show e3 as an option
    getRightVariables(allVariables: AstNodeDescription[], _context: CompletionContext): AstNodeDescription[]{
        let rightVariables: AstNodeDescription[] = [];
        if(isSupport(_context.node)){
            if(_context.node.left.ref !== undefined){
                let supporter_kind = _context.node.left.ref.kind;
                let allowable_types = this.typeMap.get(supporter_kind);

                allVariables.forEach((variable)=>{
                    if(isVariable(variable.node)){
                        if(allowable_types?.includes(variable.node.kind)){
                            rightVariables.push(variable);
                        } 
                    }
                });
            }
        }
        
        return rightVariables;
    }

    //only gives variables which can actually support other variables for autocompletion 
    //ex. if your current document only defines evidence 'e1', strategy 'e2', 
    //the autocompletion will only show evidence e1 as an autocompletion for the left support element
    getLeftVariables(allVariables: AstNodeDescription[], _context: CompletionContext): AstNodeDescription[]{
        let leftVariables: AstNodeDescription[] = [];

        allVariables.forEach((variable) =>{
            if(isVariable(variable.node)){
                let variable_kind = variable.node.kind;
                let allowable_types = this.typeMap.get(variable_kind);
                
                allVariables.forEach((possible_variable)=>{
                    if(isVariable(possible_variable.node)){
                        if(allowable_types?.includes(possible_variable.node.kind)){
                            leftVariables.push(variable);
                        }
                    }
                });
            }
        });
        return leftVariables;
    }
}