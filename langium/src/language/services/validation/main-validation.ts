import type { JpipeServices } from '../../jpipe-module.js';
import { PatternValidator, JustificationVariableValidator, SupportValidator, ImplementationValidator, JustificationValidator } from './validators/index.js';
import { AstNodeType, Validator } from './validators/abstract-validator.js';
import { JpipeAstType } from '../../generated/ast.js';

//Class to register validation services
export class JpipeValidationRegistrar{
    private readonly validators: Array<Validator<AstNodeType<keyof JpipeAstType>, keyof JpipeAstType>>;
    
    constructor(services: JpipeServices){
        const registry = services.validation.ValidationRegistry;

        this.validators = new Array<Validator<any, any>>(
            new SupportValidator("Support"),
            new JustificationVariableValidator("Variable"),
            new PatternValidator("JustificationPattern"),
            new ImplementationValidator("Implementation"),
            new JustificationValidator("JustificationPattern")
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