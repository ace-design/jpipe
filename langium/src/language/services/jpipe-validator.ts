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
        Model: [validator.allChecks]
    };
    registry.register(checks, validator);
}

export class JpipeValidator {

    //edit to implement validation hierarchy (no duplicate statements)
    allChecks(model: Model, accept: ValidationAcceptor): void{
        this.checkSupportStatements(model, accept);
    }

    //Checks that variables are defined
    private checkSupportStatements(model: Model, accept: ValidationAcceptor): void{
        model.entries.forEach( (entry) =>{
            if(hasSupports(entry)){
                entry.body.supports.forEach( (support) =>{
                    if(this.referencesCorrect(support, accept)){
                        this.checkSupportRelations(support, accept);
                    }            
                });
            }
        });
    }

    //helper function to test if variables are defined
    private referencesCorrect(support: Support, accept: ValidationAcceptor): boolean{
        let symbolNamesCorrect: boolean;

        try{
            let error = this.getErrorType(support.left, support.right);
            if(error === ErrorType.NoError){
                symbolNamesCorrect = true;
            }else{
                symbolNamesCorrect = false;
                
                let errorStatements = this.getError(error).call(this,support);
                
                errorStatements.forEach(statement =>{
                    accept("error", statement, {node: support});
                });
            }
        }catch(error: any){
            symbolNamesCorrect = false;
            accept("error", "Unknown issue with phrase", {node: support});
        }

        return symbolNamesCorrect;
    }

    //checks if support statements follow proper typing convention
    private checkSupportRelations(support: Support, accept: ValidationAcceptor): void{
        if(!this.supportRelationCorrect(support)){
            let error_message = this.getRelationErrorMessage(support);
            accept("error", error_message, {node:support});
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

    private supportRelationCorrect(support: Support): boolean{
        let supportCorrect: boolean;

        if(support.left.ref !== undefined && support.right.ref !==undefined){
            let rightKind = support.right.ref.kind;
            let possibleRights = possible_supports.get(support.left.ref.kind);

                if(possibleRights?.includes(rightKind)){
                    supportCorrect = true;
                }else{
                    supportCorrect = false;
                }
        }else{
            supportCorrect = false;
        }

        return supportCorrect;
    }

    private getRelationErrorMessage(support: Support): string{
        let error_message: string = "";
        
        if(support.left.ref !== undefined && support.right.ref !== undefined){
            error_message = `It is not possible to have ${support.left.ref.kind} support ${support.right.ref.kind}.`
        }
        
        return error_message;
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