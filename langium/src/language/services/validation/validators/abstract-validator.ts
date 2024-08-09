import { AstNode, ValidationAcceptor, ValidationCheck, ValidationChecks } from "langium";
import { JpipeAstType } from "../../../generated/ast.js";

//AstNode Type is an AstNode with a corresponding key in the JpipeAstType
export type AstNodeType<K extends keyof JpipeAstType> = JpipeAstType[K] extends AstNode ? JpipeAstType[K]: never;

//Abstract validator which automatically sets up validation checks
export abstract class Validator<T extends AstNodeType<K>, K extends keyof JpipeAstType>{
    public readonly checks: ValidationChecks<JpipeAstType>;
    private readonly check: ValidationCheck<T> = this.validate;

    constructor(key: K){
        this.checks = {
            [key]: this.check
        }
    }

    //function to provide validation for the given node
    abstract validate(model: T, accept: ValidationAcceptor): void;
}
