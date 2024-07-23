import { type ValidationAcceptor, type ValidationChecks } from 'langium';
import { isSupport, Support, type JpipeAstType, type Model} from '../generated/ast.js';
import type { JpipeServices } from '../jpipe-module.js';
import { hasSupports } from './jpipe-completion-provider.js';


/**
 * Register custom validation checks.
 */
export function registerValidationChecks(services: JpipeServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.JpipeValidator;
    const checks: ValidationChecks<JpipeAstType> = {
        Model: [validator.testChecks]
    };
    registry.register(checks, validator);
}

export class JpipeValidator {

    //edit to implement validation hierarchy (no duplicate statements)
    public allChecks(model: Model, accept: ValidationAcceptor): void{
        this.checkVariables(model, accept);
        this.checkSupportingStatements(model, accept);
    }

    public testChecks(model: Model, accept: ValidationAcceptor): void{
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
    private checkSupport(support: Support, accept: ValidationAcceptor): void{
        if(this.hasError(support)){
            let errorStatement = this.getErrorStatement(support);
            accept("error", errorStatement, {node: support});
        }
    }

    //helper function to determine if there is an error in a support statement
    private hasError(support: Support): boolean{
        let left = support.left.ref;
        let right = support.right.ref;

        return left === undefined || right === undefined;
    }

    //helper function to determine the necessary error statement
    private getErrorStatement(support: Support): string{
        let errorStatement: string

        let left = support.left.ref;
        let right = support.right.ref;
        
        if(left === undefined && right === undefined){
            errorStatement = `Left and right variable are undefined.`
        }else if(left === undefined){
            errorStatement = `Left variable is undefined.`
        }else{
            errorStatement = `Right variable is undefined.`
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