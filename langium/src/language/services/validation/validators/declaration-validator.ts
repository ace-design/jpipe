import { diagnosticData, ValidationAcceptor } from "langium";
import { Implementation} from "../../../generated/ast.js";
import { Validator } from "./abstract-validator.js";

//Class to validate declarations in the jpipe language
export class ImplementationValidator extends Validator<Implementation, "Implementation">{
    //function to validate the document
    validate(implementation: Implementation, accept: ValidationAcceptor): void {
        let declaration = implementation.$container;

        if(!(declaration.kind === "justification" || declaration.kind === "pattern")){ //validating that justification is implementing
            accept("error", "Compositions cannot implement other declarations", {node: implementation, data: diagnosticData("compositionImplementing")});
        }

        if(implementation.implemented.ref){
            if(implementation.implemented.ref.kind !== "pattern"){
                accept("error", "Only patterns can be implemented by other declarations", {node: implementation, data: diagnosticData("nonPatternImplemented")})
            }
            
        }  
    }
}