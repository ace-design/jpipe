import { Reference, type ValidationAcceptor, type ValidationChecks } from 'langium';
import { isSupport, Support, Variable, type JpipeAstType, type Model} from '../generated/ast.js';
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
        this.checkVariables(model, accept);

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
        try{
            let error = this.getErrorType(support.left, support.right);
            if(error !== ErrorType.NoError){
                let errorStatements = this.getError(error).call(this,support);
                
                errorStatements.forEach(statement =>{
                    accept("error", statement, {node: support});
                })
            }
        }catch(error: any){
            accept("error", "Unknown issue with phrase", {node: support});
        }
    }

    //helper function to determine if there is an error in a support statement
    private getErrorType(left: Reference<Variable>, right: Reference<Variable>): ErrorType{
        let errorType: ErrorType;

        if(left.ref === undefined || right.ref === undefined){
            errorType = ErrorType.ReferenceError;
        }else{
            errorType = ErrorType.NoError;
        }

        return errorType;
    }

    //helper function to determine the necessary error statement
    private getError(errorType: ErrorType): (support: Support) => string[]{
        let errorFunction: (support: Support) => string[];

        let errors = new Map<ErrorType, (support: Support) => string[]>([
            [ErrorType.ReferenceError, this.getReferenceError]
        ]);  
        
        let returnFunction = errors.get(errorType);
        
        if(returnFunction){
            errorFunction = returnFunction;
        }else{
            errorFunction =  () => [];
        }
        
        return errorFunction;
    }
    
    private getReferenceError =  (support: Support): string[] => {
        let errorStatement: string[];

        let left = support.left;
        let right = support.right;
        
        if(left.ref === undefined && right.ref === undefined){
            errorStatement =[ `Variables ${left.$refText} and ${right.$refText} are undefined.`];
        }else if(left.ref === undefined){
            errorStatement = [`Variable ${left.$refText} is undefined.`];
        }else{
            errorStatement = [`Variable ${right.$refText}is undefined.`];
        }

        return errorStatement;
    };

    //checks if support statements follow proper typing convention
    private checkSupportingStatements(model: Model, accept: ValidationAcceptor): void{
        model.entries.forEach( (entry) =>{
            if(hasSupports(entry)){
                entry.body.supports.forEach( (support) =>{
                    if(isSupport(support)){
                        if(support.left.ref !== undefined && support.right.ref !==undefined){
                            let leftKind = support.left.ref.kind;
                            let rightKind = support.right.ref.kind;
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

enum ErrorType{
    NoError,
    ReferenceError
}


export var possible_supports = new Map<string,string[]>([
    ['evidence', ['strategy', '@support']],
    ['strategy', ['sub-conclusion', 'conclusion', '@support']],
    ['sub-conclusion', ['strategy', 'conclusion', '@support']],
    ['conclusion', []] ,
    ['@support', ['strategy', 'sub-conclusion', 'conclusion']]
]);