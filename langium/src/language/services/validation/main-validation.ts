import { AstNode, ValidationAcceptor, ValidationChecks } from 'langium';
import type { JpipeServices } from '../../jpipe-module.js';
import { SupportValidator } from './support-validator.js';
import { Class, CompositionClass, CompositionInstruction, Instruction, isClass, isCompositionClass, isCompositionInstruction, isInstruction, JpipeAstType } from '../../generated/ast.js';
import { JustificationVariableValidator } from './variable-validator.js';

/**
 * Register custom validation checks.
 */
export function registerValidationChecks(services: JpipeServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.validator;

    registry.register(validator.checks, validator);
}

export class JpipeValidator{
    public static support_validator = new SupportValidator();
    public static justification_validator = new JustificationVariableValidator();

    public readonly checks: ValidationChecks<JpipeAstType> = {
        Variable: JpipeValidator.justification_validator.validate,
        Support: JpipeValidator.support_validator.validate
    }
}

//when creating a validator, implement this interface
export interface Validator<T>{
    //create a list of checks your validator makes ***must be of type T
    checks: ValidationChecks<JpipeAstType>;

    //function which actually validates the given information
    validate(model: T, accept: ValidationAcceptor): void;
}


//function to determine if a given node is an instruction
export function isInstructionType(node: AstNode | undefined): node is Instruction | CompositionInstruction{
    return isInstruction(node) || isCompositionInstruction(node);
}


//function to determine if a given node is a class
export function isClassType(node: AstNode | undefined): node is Class | CompositionClass{
    return isClass(node) || isCompositionClass(node);
}


//data structure to represent which elements can support which, format is ['a', ['b', 'c']], where "a supports b" or "a supports c"
export var possible_supports = new Map<string,string[]>([
    ['evidence', ['strategy', '@support']],
    ['strategy', ['sub-conclusion', 'conclusion', '@support']],
    ['sub-conclusion', ['strategy', 'conclusion', '@support']],
    ['conclusion', []] ,
    ['@support', ['strategy', 'sub-conclusion', 'conclusion']]
]);