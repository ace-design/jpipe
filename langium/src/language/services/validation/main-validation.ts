import type { JpipeServices } from '../../jpipe-module.js';
import { PatternValidator, JustificationVariableValidator, SupportValidator, DeclarationValidator } from './validators/index.js';
import { AstNodeType, Validator } from './validators/abstract-validator.js';
import { JpipeAstType } from '../../generated/ast.js';

export class JpipeValidationRegistrar{
    private validators: Array<Validator<keyof JpipeAstType, AstNodeType<keyof JpipeAstType>>>;
    constructor(services: JpipeServices){
        const registry = services.validation.ValidationRegistry;

        this.validators = new Array<Validator<any, any>>(
            new SupportValidator("Support"),
            new JustificationVariableValidator("Variable"),
            new PatternValidator("JustificationPattern"),
            new DeclarationValidator("Declaration")
        );

        this.validators.forEach(validator =>{
            registry.register(validator.checks)
        })
    }
}


//data structure to represent which elements can support which, format is ['a', ['b', 'c']], where "a supports b" or "a supports c"
export var possible_supports = new Map<string,string[]>([
    ['evidence', ['strategy', '@support']],
    ['strategy', ['sub-conclusion', 'conclusion', '@support']],
    ['sub-conclusion', ['strategy', 'conclusion', '@support']],
    ['conclusion', []] ,
    ['@support', ['strategy', 'sub-conclusion', 'conclusion']]
]);