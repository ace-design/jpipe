import { AstNode, CstNode, LangiumDocument, ValidationAcceptor, ValidationCheck, ValidationChecks } from 'langium';
import type { JpipeServices } from '../../jpipe-module.js';
import { JpipeAstType, Variable } from '../../generated/ast.js';
import { PatternValidator, JustificationVariableValidator, SupportValidator, DeclarationValidator } from './validators/index.js';

export class JpipeValidationRegistrar{
    private validators: Array<Validator<AstNode>>;
    private checks: ValidationChecks<JpipeAstType>;
    constructor(services: JpipeServices){
        const registry = services.validation.ValidationRegistry;
        
        this.checks = {

        }

        this.validators = new Array<Validator<AstNode>>(
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

type AstNodeKey<K extends keyof JpipeAstType, T extends JpipeAstType[K]> = {[K in keyof T]: T[K] extends AstNode ? T[K]: never}

export abstract class Validator1<K extends keyof JpipeAstType, T extends JpipeAstType[K], V extends AstNodeKey<K, T>>{
    private readonly checks: ValidationChecks<JpipeAstType>;
    private readonly check: ValidationCheck<V> = this.validate;

    constructor(key: K){
        this.checks = {
            [key]: this.check
        }
    }

    abstract validate(model: V, accept: ValidationAcceptor): void;
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