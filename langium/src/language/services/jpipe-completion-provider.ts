import { AstNode, AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { CompositionClass, CompositionInstruction, isCompositionClass, isCompositionInstruction, isJustification, isJustificationClass, isJustificationImplementsClass, isJustificationInstruction, isJustificationSupport, isJustificationVariable, isPattern, isPatternClass, isPatternInstruction, isPatternSupport, isPatternVariable, Justification, JustificationClass, JustificationImplementsClass, JustificationInstruction, JustificationSupport, JustificationVariable, Pattern, PatternClass, PatternInstruction, PatternSupport, PatternVariable } from "../generated/ast.js";
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
        let strict_left_filtering = false; //to be added as a configuration option
        
        let potential_references = this.scopeProvider.getScope(refInfo).getAllElements();

        let references = this.findKeywords(potential_references);
        let variables = this.findVariables(potential_references);

        //if the current context is of a supporting statement, determines which variables should appear for autocomplete
        if(isSupport(_context.node)){
            let support_variables: AstNodeDescription[];
            
            if(this.onRightSide(_context.node)){
                support_variables = this.getRightVariables(variables, _context);
            }
            else{
                support_variables = this.getLeftVariables(variables, strict_left_filtering);
            }

            support_variables.forEach(v =>{
                references.push(v);
            });
        }
        
        return stream(references);
    }

    //helper function for filtering references
    findVariables(potential_references: Stream<AstNodeDescription>): AstNodeDescription[]{
        let variables: AstNodeDescription[]=[];

        potential_references.forEach((potential) =>{
            if(isVariable(potential.node)){
                variables.push(potential);
            }
        });

        return variables;
    }

    //helper function for finding non-variable keywords
    findKeywords(potential_references: Stream<AstNodeDescription>): AstNodeDescription[]{
        let keywords: AstNodeDescription[] = [];

        potential_references.forEach(potential =>{
            if(!isVariable(potential.node)){
                keywords.push(potential);
            }
        });

        return keywords;
    }

    //helper functino to determine which side of support statement we are on
    onRightSide(context_node: PatternSupport | JustificationSupport){
        return context_node.left.ref !== undefined;
    }
   
    
    //autocompletes right-side variables so that only those which fit the format are shown
    //ex. if your JD defines evidence 'e1', strategy 'e2' and conclusion e3', when starting the statement:
    //e2 supports ___ auto-completion will only show e3 as an option
    getRightVariables(variables: AstNodeDescription[], _context: CompletionContext): AstNodeDescription[]{
        let rightVariables: AstNodeDescription[] = [];
        
        if(isSupport(_context.node)){
            
            if(_context.node.left.ref !== undefined){
                let supporter_kind = _context.node.left.ref.kind;
                let allowable_types = this.typeMap.get(supporter_kind);

                rightVariables = this.findRightVariables(variables, allowable_types);
            }
        }
        
        return rightVariables;
    }

    //helperFunction for getRightVariables
    findRightVariables(variables: AstNodeDescription[], allowable_types: string[] | undefined){
        let right_variables: AstNodeDescription[] = [];
        
        variables.forEach((variable)=>{
            if(isVariable(variable.node)){
                if(allowable_types?.includes(variable.node.kind)){
                    right_variables.push(variable);
                } 
            }
        });

        return right_variables;
    }

    //only gives variables which can actually support other variables for autocompletion 
    //ex. if your current document only defines evidence 'e1', strategy 'e2', 
    //the autocompletion will only show evidence e1 as an autocompletion for the left support element
    getLeftVariables(variables: AstNodeDescription[], strict_left_filtering: boolean): AstNodeDescription[]{
        let left_variables: AstNodeDescription[];

        if(strict_left_filtering){
            left_variables = this.filterLeftProbableVariables(variables);
        }
        else{
            left_variables = this.filterLeftPossibleVariables(variables);
        }

        return left_variables;
    }

    //helper function for getLeftVariables: checks if the variable 
    //could have a potential matching right variable, regardless if this variable
    //has already been defined or not
    filterLeftPossibleVariables(variables: AstNodeDescription[]): AstNodeDescription[]{
        let left_variables: AstNodeDescription[] = [];
        
        variables.forEach(variable =>{
            if(isVariable(variable.node)){
                let variable_kind = variable.node.kind;
                let allowable_types = this.typeMap.get(variable_kind);
                
                if (!(allowable_types === undefined  || allowable_types.length === 0)){
                    left_variables.push(variable);
                }
            }
        });

        return left_variables;
    }

    //helper function for getLeftVariables: checks if the variable could have 
    //a potential matching right variable from the existing defined variables
    filterLeftProbableVariables(variables: AstNodeDescription[]): AstNodeDescription[]{
        let left_variables: AstNodeDescription[] = [];

        variables.forEach((variable) =>{
            if(isVariable(variable.node)){
                let allowable_types = this.typeMap.get(variable.node.kind);
                
                if (this.hasRightVariableDefined(variables,allowable_types)){
                    left_variables.push(variable);
                }
            }
        });

        return left_variables;
    }

     //helper function for filtering left probable variables
     hasRightVariableDefined(variables: AstNodeDescription[], allowable_types: string[] | undefined): boolean{        
        let result:boolean = false;
        
        variables.forEach(possible_variable=>{
            if(isVariable(possible_variable.node)){
                if(allowable_types?.includes(possible_variable.node.kind)){
                    result = true;
                }
            }
        });

        return result;
    }
}

export function isSupport(node: AstNode | undefined): node is JustificationSupport | PatternSupport{
    return isJustificationSupport(node) || isPatternSupport(node);
}

export function isVariable(node: AstNode | undefined): node is JustificationVariable | PatternVariable {
    return isJustificationVariable(node) || isPatternVariable(node);
}

export function isInstruction(node: AstNode | undefined): node is JustificationInstruction | PatternInstruction | CompositionInstruction{
    return isJustificationInstruction(node) || isPatternInstruction(node) || isCompositionInstruction(node);
}


export function isClass(node: AstNode | undefined): node is JustificationClass | JustificationImplementsClass | PatternClass | CompositionClass{
    return isJustificationClass(node) || isPatternClass(node) || isJustificationImplementsClass(node) || isCompositionClass(node);
}

export function hasSupports(node: AstNode): node is Pattern | Justification{
    return isPattern(node) || isJustification(node);
}