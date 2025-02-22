import { diagnosticData, ValidationAcceptor } from "langium";
import { JustificationPattern, Variable } from "../../../generated/ast.js";
import { Validator } from "./abstract-validator.js";

//Class to validate patterns in the jpipe language
export class PatternValidator extends Validator<JustificationPattern, "JustificationPattern">{
    //function to provide validation of a given node
    validate(model: JustificationPattern, accept: ValidationAcceptor): void {
        if(model.kind === "pattern"){
            PatternValidator.checkConclusion(model, accept);
            PatternValidator.checkSupport(model, accept);
        }
    }

    //helper function to check existence of support variable
    private static checkSupport(model: JustificationPattern, accept: ValidationAcceptor): void{
        let support_found = false;

        model.variables.forEach( variable => {
            if(variable.kind === "@support"){
                support_found = true;
            }
        });

        if(!support_found){
            accept("warning", "No @support variables found in pattern", {node: model, property: "name", data: diagnosticData("noSupportInPattern")})
        }
    }

    //helper function to check if conclusion exists in a pattern
    private static checkConclusion(model: JustificationPattern, accept: ValidationAcceptor): void{
        let conclusion_exists = PatternValidator.conclusionExists(model.variables);

        if(!conclusion_exists){
            accept("error", "All justification diagrams must have a conclusion", {node: model, property: "name", data: diagnosticData("noConclusionInPattern")})
        }
    }
    
    //helper function to determine if a conclusion exists in a given set of variables
    private static conclusionExists(variables: Array<Variable>): boolean{
        let conclusion_exists: boolean = false;
        let i = 0;

        while(i < variables.length && !conclusion_exists){
            let variable = variables[i];
            
            if(variable.kind === "conclusion"){
                conclusion_exists = true;
            }

            i++
        }

        return conclusion_exists;
    }
}