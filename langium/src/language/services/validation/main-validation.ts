import { ValidationAcceptor, ValidationChecks } from 'langium';
import type { JpipeServices } from '../../jpipe-module.js';
import { SupportValidator } from './validators/support-validator.js';
import { JpipeAstType } from '../../generated/ast.js';
import { JustificationVariableValidator } from './validators/variable-validator.js';
import { PatternValidator } from './validators/pattern-validator.js';

/**
 * Register custom validation checks.
 */
export function registerValidationChecks(services: JpipeServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.validator;

    registry.register(validator.checks, validator);
}

//Register additional validation here
export class JpipeValidator{
    public static support_validator = new SupportValidator();
    public static justification_validator = new JustificationVariableValidator();
    public static pattern_validator = new PatternValidator();
    
    public readonly checks: ValidationChecks<JpipeAstType> = {
        Variable: JpipeValidator.justification_validator.validate,
        Support: JpipeValidator.support_validator.validate,
        JustificationPattern: JpipeValidator.pattern_validator.validate
    }
}

//when creating a validator, implement this interface
export interface Validator<T>{
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