import { diagnosticData, ValidationAcceptor, ValidationChecks } from "langium";
import { Declaration, JpipeAstType } from "../../../generated/ast.js";
import { Validator } from "../main-validation.js";

export class DeclarationValidator implements Validator<Declaration>{
    public readonly checks: ValidationChecks<JpipeAstType> = {
        Declaration: this.validate,
    }
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