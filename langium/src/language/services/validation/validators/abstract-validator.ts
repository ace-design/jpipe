import { AstNode, ValidationAcceptor, ValidationCheck, ValidationChecks } from "langium";
import { JpipeAstType } from "../../../generated/ast.js";

export type AstNodeType<K extends keyof JpipeAstType> = JpipeAstType[K] extends AstNode ? JpipeAstType[K]: never;

export abstract class Validator<K extends keyof JpipeAstType, T extends AstNodeType<K>>{
    public readonly checks: ValidationChecks<JpipeAstType>;
    private readonly check: ValidationCheck<T> = this.validate;

    constructor(key: K){
        this.checks = {
            [key]: this.check
        }
    }

    abstract validate(model: T, accept: ValidationAcceptor): void;
}
