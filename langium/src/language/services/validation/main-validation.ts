import { AstNode, ValidationAcceptor, ValidationChecks } from 'langium';
import type { JpipeServices } from '../../jpipe-module.js';
import { JpipeAstType } from '../../generated/ast.js';
import { PatternValidator, JustificationVariableValidator, SupportValidator, DeclarationValidator } from './validators/index.js';

export class JpipeValidationRegistrar{
    private validators: Array<Validator<any>>;
    
    constructor(services: JpipeServices){
        const registry = services.validation.ValidationRegistry;

        this.validators = new Array<Validator<any>>(
            new SupportValidator(),
            new JustificationVariableValidator(),
            new PatternValidator(),
            new DeclarationValidator()
        );

        this.validators.forEach(validator =>{
            registry.register(validator.checks)
        })
    }
}

//when creating a validator, implement this interface
export interface Validator<T extends AstNode>{
    checks: ValidationChecks<JpipeAstType>;
    //function which actually validates the given information
    validate(model: T, accept: ValidationAcceptor): void;
}

//data structure to represent which elements can support which, format is ['a', ['b', 'c']], where "a supports b" or "a supports c"
export var possible_supports = new Map<string,string[]>([
    ['evidence', ['strategy', '@support']],
    ['strategy', ['sub-conclusion', 'conclusion', '@support']],
    ['sub-conclusion', ['strategy', 'conclusion', '@support']],
    ['conclusion', []] ,
    ['@support', ['strategy', 'sub-conclusion', 'conclusion']]
]);