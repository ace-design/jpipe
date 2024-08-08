import { diagnosticData, ValidationAcceptor, ValidationChecks } from "langium";
import { JpipeAstType, Variable } from "../../../generated/ast.js";
import { Validator } from "../main-validation.js";

//class to validate variables in justification diagrams
export class JustificationVariableValidator implements Validator<Variable>{
    public readonly checks: ValidationChecks<JpipeAstType> = {
        Variable: this.validate,
    } 
    //actual function to validate the variable
    public validate(variable: Variable, accept: ValidationAcceptor): void {
        let declaration_kind = variable.$container.kind;
        if(declaration_kind === 'justification'){
             JustificationVariableValidator.validateJustificationVariables(variable, accept);
        }

    }

    //helper function to validate variables within justification diagrams
    private static validateJustificationVariables(variable: Variable, accept: ValidationAcceptor){
        if(JustificationVariableValidator.isJustificationVariableAcceptable(variable)){
            let error_message = "Variable kind: " + variable.kind + " is not included in a " + variable.$container.kind + " diagram";
            
            accept("error", error_message, {node: variable, property: "kind", data: diagnosticData("supportInJustification")});
        }
    }

    //helper function to determine if a variable is acceptable within a justification diagram
    private static isJustificationVariableAcceptable(variable: Variable): boolean{
        let variable_acceptable: boolean;

        let acceptable_kinds = ["evidence", "strategy", "sub-conclusion", "conclusion"];

        if(!acceptable_kinds.includes(variable.kind)){
            variable_acceptable = true;
        }else{
            variable_acceptable = false;
        }
        
        return variable_acceptable;
    }
}