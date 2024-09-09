import { diagnosticData, ValidationAcceptor } from "langium";
import { JustificationPattern, Variable } from "../../../generated/ast.js";
import { Validator } from "./abstract-validator.js";

//validator class for justifications
export class JustificationValidator extends Validator<JustificationPattern, "JustificationPattern">{
    //validation function for justification
    public override validate(model: JustificationPattern, accept: ValidationAcceptor): void {
        if(model.kind === "justification"){
            let conclusion_exists = JustificationValidator.conclusionExists(model.variables);

            if(!conclusion_exists){
                accept("error", "All justification diagrams must have a conclusion", {node: model, property: "name", data: diagnosticData("noConclusionInJustification")})
            }
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