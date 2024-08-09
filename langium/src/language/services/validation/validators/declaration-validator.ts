import { diagnosticData, ValidationAcceptor } from "langium";
import { Declaration } from "../../../generated/ast.js";
import { Validator } from "./abstract-validator.js";

//Class to validate declarations in the jpipe language
export class DeclarationValidator extends Validator<Declaration, "Declaration">{
    //function to validate the document
    validate(declaration: Declaration, accept: ValidationAcceptor): void {
        if(declaration.implemented){
            if(declaration.kind !== "justification"){ //validating that justification is implementing
                accept("error", "Only justifications can implement other declarations", {node: declaration, property: "kind", data: diagnosticData("nonJustificationImplementing")});
            }

            if(declaration.implemented.ref){
                if(declaration.implemented.ref.kind !== "pattern"){
                    accept("error", "Only patterns can be implemented by other declarations", {node: declaration, property: "implemented", data: diagnosticData("nonPatternImplemented")})
                }
                
            }
        }
    }
    
}