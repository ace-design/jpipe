import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { isSupport, isVariable, Variable } from "../../generated/ast.js";
import { JpipeCompletion } from "./jpipe-completion-provider.js";
import { possible_supports } from "../validation/main-validation.js";


export class SupportCompletionProvider implements JpipeCompletion{
    //returns all candidates from the given reference type
    public getCandidates(potential_references: Stream<AstNodeDescription>, refInfo: ReferenceInfo): Set<AstNodeDescription> {
        let strict_left_filtering = true;
        let support_variables = new Set<AstNodeDescription>();

        let variables = this.findVariables(potential_references);
        if(refInfo.property === "right"){
            if(isSupport(refInfo.container)){
                if(isVariable(refInfo.container.left.ref)){
                    support_variables = this.getRightVariables(variables, refInfo.container.left.ref);
                }
            } 
        }else{
            support_variables = this.getLeftVariables(variables, strict_left_filtering);
        }

        return support_variables;
    }

    //helper function for filtering references
    private findVariables(potential_references: Stream<AstNodeDescription>): Set<AstNodeDescription>{
        let variables = new Set<AstNodeDescription>();

        potential_references.forEach((potential) =>{
            if(isVariable(potential.node)){
                variables.add(potential);
            }
        });

        return variables;
    }

    //autocompletes right-side variables so that only those which fit the format are shown
    //ex. if your JD defines evidence 'e1', strategy 'e2' and conclusion e3', when starting the statement:
    //e2 supports ___ auto-completion will only show e3 as an option
    private getRightVariables(variables: Set<AstNodeDescription>, node: Variable): Set<AstNodeDescription>{
        let rightVariables = new Set<AstNodeDescription>();

        let supporter_kind = node.kind;
        let allowable_types = possible_supports.get(supporter_kind);

        rightVariables = this.findRightVariables(variables, allowable_types);
        
        return rightVariables;
    }


     //helperFunction for getRightVariables
     private findRightVariables(variables: Set<AstNodeDescription>, allowable_types: string[] | undefined): Set<AstNodeDescription>{
        let right_variables = new Set<AstNodeDescription>();
        
        variables.forEach((variable)=>{
            if(isVariable(variable.node)){
                if(allowable_types?.includes(variable.node.kind)){
                    right_variables.add(variable);
                }
            }
        });

        return right_variables;
    }

     //only gives variables which can actually support other variables for autocompletion 
    //ex. if your current document only defines evidence 'e1', strategy 'e2', 
    //the autocompletion will only show evidence e1 as an autocompletion for the left support element
    private getLeftVariables(variables: Set<AstNodeDescription>, strict_left_filtering: boolean): Set<AstNodeDescription>{
        let left_variables: Set<AstNodeDescription>;

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
    private filterLeftPossibleVariables(variables: Set<AstNodeDescription>): Set<AstNodeDescription>{
        let left_variables = new Set<AstNodeDescription>();
        
        variables.forEach(variable =>{
            if(isVariable(variable.node)){
                let variable_kind = variable.node.kind;
                let allowable_types = possible_supports.get(variable_kind);
                
                if (!(allowable_types === undefined  || allowable_types.length === 0)){
                    left_variables.add(variable);
                }
            }
        });

        return left_variables;
    }

    //helper function for getLeftVariables: checks if the variable could have 
    //a potential matching right variable from the existing defined variables
    private filterLeftProbableVariables(variables: Set<AstNodeDescription>): Set<AstNodeDescription>{
        let left_variables = new Set<AstNodeDescription>();

        variables.forEach((variable) =>{
            if(isVariable(variable.node)){
                let allowable_types = possible_supports.get(variable.node.kind);
                
                if (this.hasRightVariableDefined(variables, allowable_types)){
                    left_variables.add(variable);
                }
            }
        });

        return left_variables;
    }

    //helper function for filtering left probable variables
    private hasRightVariableDefined(variables: Set<AstNodeDescription>, allowable_types: string[] | undefined): boolean{        
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