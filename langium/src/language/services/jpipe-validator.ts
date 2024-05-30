import { type ValidationAcceptor, type ValidationChecks } from 'langium';
import type { JpipeAstType, Model} from '../generated/ast.js';
import type { JpipeServices } from '../jpipe-module.js';


/**
 * Register custom validation checks.
 */
export function registerValidationChecks(services: JpipeServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.JpipeValidator;
    const checks: ValidationChecks<JpipeAstType> = {
        Model: [validator.allChecks]
    };
    registry.register(checks, validator);
}

export class JpipeValidator {

    //edit to implement validation hierarchy (no duplicate statements)
    allChecks(model: Model, accept: ValidationAcceptor): void{
        //this.checkNaming(model, accept);
        this.checkVariables(model, accept);
        this.checkSupportingStatements(model, accept);
    }

    //verifies variable naming
    checkNaming(model: Model, accept: ValidationAcceptor): void{
        model.entries.forEach( (entry) =>{
            entry.variables.forEach((variable)=>{
                let variableName = variable.name;
                let pattern = /^[A-Z][a-z]*/;
                if(!pattern.test(variableName)){
                    accept("warning", "Your name does not match the naming requirements (must start with a capital)", {
                        node: variable
                    });
                };
            });
        });
    }

    //theoretically checks that variables are defined
    checkVariables(model: Model, accept: ValidationAcceptor): void{
        model.entries.forEach( (entry) =>{
            entry.supports.forEach( (support) =>{
                if(support.left.ref !== undefined && support.right.ref !==undefined){
                    let leftKind = support.left.ref?.kind;
                    let rightKind = support.right.ref?.kind;
                    if(leftKind === undefined || rightKind === undefined){
                        if(leftKind === undefined && rightKind === undefined){
                            accept("error", `Variables ${support.left.$refText} and ${support.right.$refText} are undefined.`, {
                                node:support
                            });
                        }else if(leftKind === undefined){
                            accept("error", `Variable ${support.left.$refText} is undefined.`, {
                                node:support
                            });
                        }else if(rightKind === undefined){
                            accept("error", `Variable ${support.right.$refText} is undefined.`, {
                                node:support
                            });
                        }
        
                    }
                }

            });
        });
    }

    //checks if support statements follow proper typing convention
    checkSupportingStatements(model: Model, accept: ValidationAcceptor): void{
        let possibleSupports: Map<string, string[]> = new Map<string,string[]>([
            ['evidence', ['strategy']],
            ['strategy', ['sub-conclusion', 'conclusion']],
            ['sub-conclusion', ['strategy', 'conclusion']],
            ['conclusion', []] 
        ]);
        
        model.entries.forEach( (entry) =>{
            entry.supports.forEach( (support) =>{
                if(support.left.ref !== undefined && support.right.ref !==undefined){
                    let leftKind = support.left.ref?.kind;
                    let rightKind = support.right.ref?.kind;
                    let possibleRights: string[] | undefined = possibleSupports.get(leftKind);
                        
                    if(rightKind !== undefined){
                        if (possibleRights?.includes(rightKind)){
                            return;
                        }
                    }
                    
                    accept("error", `It is not possible to have ${leftKind} support ${rightKind}.`, {
                        node:support
                    });
                }
                
            });
        });
    }


}
