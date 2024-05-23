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
        Model: [validator.checkNaming, validator.checkVariables, validator.checkSupportingStatements]
    };
    registry.register(checks, validator);
}

/**
 * Implementation of custom validations.
 */
export class JpipeValidator {
    //implements the checking hierarchy (no duplicate statements)
    allChecks(): void{

    }

    checkNaming(model: Model, accept: ValidationAcceptor): void{
        model.declarations.forEach((declaration)=>{
            let variableName =  declaration.name;
            let pattern = /^[A-Z][a-z]*/;
            if(!pattern.test(variableName)){
                accept("warning", "Your name does not match the naming requirements (must start with a capital)", {
                    node: declaration
                });
            };
        });  
    }
    checkVariables(model: Model, accept: ValidationAcceptor): void{
        model.supports.forEach( (support) =>{
            let supporterType = support.supporter.ref?.type;
            let supporteeType = support.supportee.ref?.type;
            if(supporterType === undefined || supporteeType === undefined){
                if(supporterType === undefined && supporteeType === undefined){
                    accept("error", `Variables ${support.supporter.$refText} and ${support.supportee.$refText} are undefined.`, {
                        node:support
                    });
                }else if(supporterType === undefined){
                    accept("error", `Variable ${support.supporter.$refText} is undefined.`, {
                        node:support
                    });
                }else if(supporteeType === undefined){
                    accept("error", `Variable ${support.supportee.$refText} is undefined.`, {
                        node:support
                    });
                }

            }
        });
    }

    checkSupportingStatements(model: Model, accept: ValidationAcceptor): void{
        //Lookup map to identify what a supporter can support
        let possibleSupports: Map<string | undefined, string[]> = new Map<string, string[]>();
        possibleSupports.set('evidence', ['strategy']);
        possibleSupports.set('strategy', ['sub-conclusion', 'conclusion']);
        possibleSupports.set('sub-conclusion', ['strategy','conclusion']);
        possibleSupports.set('conclusion', []);
        
        model.supports.forEach( (support) =>{
            let supporterType = support.supporter.ref?.type;
            let supporteeType = support.supportee.ref?.type;
            let possibleSupportees: string[] | undefined = possibleSupports.get(supporterType);

            if(supporteeType !== undefined){
                if (possibleSupportees?.includes(supporteeType)){
                    return;
                }
            }
            
            accept("error", `It is not possible to have ${supporterType} support ${supporteeType}.`, {
                node:support
            });
        });
    }


}
