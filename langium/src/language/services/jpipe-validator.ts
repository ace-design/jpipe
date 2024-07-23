import { Reference, type ValidationAcceptor, type ValidationChecks } from 'langium';
import { JustificationSupport, JustificationVariable, PatternSupport, PatternVariable, Variable, type JpipeAstType, type Model} from '../generated/ast.js';
import type { JpipeServices } from '../jpipe-module.js';
import { hasSupports, isSupport } from './jpipe-completion-provider.js';


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

    //Checks that variables are defined
    private checkVariables(model: Model, accept: ValidationAcceptor): void{
        model.entries.forEach( (entry) =>{
            if(hasSupports(entry)){
                entry.body.supports.forEach( (support) =>{
                    if(isSupport(support)){
                        this.checkSupport(support, accept);
                    }
                });
            }

        });
    }

    //helper function to test if variables are defined
    private checkSupport(support: PatternSupport | JustificationSupport, accept: ValidationAcceptor): void{
        if(this.hasError(support.left, support.right)){
            let errorStatement = this.getErrorStatement(support.left, support.right);
            accept("error", errorStatement, {node: support});
        }
    }

    //helper function to determine if there is an error in a support statement
    private hasError(leftSupport: Reference<JustificationVariable | PatternVariable>, rightSupport: Reference<Variable>): boolean{
        let hasError: boolean;

        let leftKind = leftSupport.ref?.kind;
        let rightKind = rightSupport.ref?.kind;

        if(leftKind === undefined || rightKind === undefined){
           hasError = true; 
        }else{
            hasError = false;
        }

        return hasError;
    }

    //helper function to determine the necessary error statement
    private getErrorStatement(leftSupport: Reference<Variable>, rightSupport: Reference<Variable>): string{
        let errorStatement: string

        let leftKind = leftSupport.ref?.kind;
        let rightKind = rightSupport.ref?.kind;
        
        if(leftKind === undefined && rightKind === undefined){
            errorStatement = `Variables ${leftSupport.$refText} and ${rightSupport.$refText} are undefined.`
        }else if(leftKind === undefined){
            errorStatement = `Variable ${leftSupport.$refText} is undefined.`
        }else{
            errorStatement = `Variable ${rightSupport.$refText} is undefined.`
        }

        return errorStatement;
    }

    //checks if support statements follow proper typing convention
    private checkSupportingStatements(model: Model, accept: ValidationAcceptor): void{
        
        model.entries.forEach( (entry) =>{
            if(hasSupports(entry)){
                entry.body.supports.forEach( (support) =>{
                    if(isSupport(support)){
                        if(support.left.ref !== undefined && support.right.ref !==undefined){
                            let leftKind = support.left.ref?.kind;
                            let rightKind = support.right.ref?.kind;
                            let possibleRights: string[] | undefined = possible_supports.get(leftKind);
                                
                            if(rightKind !== undefined){
                                if (possibleRights?.includes(rightKind)){
                                    return;
                                }
                            }
                            
                            accept("error", `It is not possible to have ${leftKind} support ${rightKind}.`, {
                                node:support
                            });
                        }
                    }
                });
            }

        });
    }
}

export var possible_supports = new Map<string,string[]>([
    ['evidence', ['strategy', '@support']],
    ['strategy', ['sub-conclusion', 'conclusion', '@support']],
    ['sub-conclusion', ['strategy', 'conclusion', '@support']],
    ['conclusion', []] ,
    ['@support', ['strategy', 'sub-conclusion', 'conclusion']]
]);