import { diagnosticData, ValidationAcceptor } from "langium";
import { JustificationPattern } from "../../generated/ast.js";
import { Validator } from "./main-validation.js";


export class PatternValidator implements Validator<JustificationPattern>{
    validate(model: JustificationPattern, accept: ValidationAcceptor): void {
        if(model.kind === "pattern"){
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
    }
    
}