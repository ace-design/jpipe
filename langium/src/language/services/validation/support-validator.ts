import { Reference, ValidationAcceptor, ValidationChecks} from "langium";
import { JpipeAstType, Support, Variable } from "../../generated/ast.js";
import { possible_supports, Validator } from "./main-validation.js";

//class to validate supporting statements found in the document
export class SupportValidator implements Validator<Support>{
    //list of checks the validator makes
    public checks: ValidationChecks<JpipeAstType> = {
        Support: [this.validate]
    };

    //validator function
    public validate(support: Support, accept: ValidationAcceptor): void {
        if(this.referencesCorrect(support, accept)){
            this.checkSupportRelations(support, accept);
        }  
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
    
    //helper function which gets the text(s) for a reference error (variable undefined etc.)
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
    private checkSupportRelations(support: Support, accept: ValidationAcceptor): void{
        if(!this.supportRelationCorrect(support)){
            let error_message = this.getRelationErrorMessage(support);
            accept("error", error_message, {node:support});
        }
    }
    
    //helper function to check if the left and the right variable are of the right kind
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

    //helper function to get the error message on incorrect relation
    private getRelationErrorMessage(support: Support): string{
        let error_message: string = "";
        
        if(support.left.ref !== undefined && support.right.ref !== undefined){
            error_message = `It is not possible to have ${support.left.ref.kind} support ${support.right.ref.kind}.`
        }
        
        return error_message;
    }
}

//enum to represent error types
enum ErrorType{
    NoError,
    ReferenceError
}