import type { AstNode, LangiumDocument, MaybePromise, ValidationChecks } from 'langium';
import type { JpipeAstType} from './generated/ast.js';
import type { JpipeServices } from './jpipe-module.js';
import { HoverProvider } from 'langium/lsp';
import { CancellationToken, Hover, HoverParams } from 'vscode-languageserver';

/**
 * Register custom validation checks.
 */
export function registerValidationChecks(services: JpipeServices) {
    const registry = services.validation.ValidationRegistry;
    const validator = services.validation.JpipeValidator;
    const checks: ValidationChecks<JpipeAstType> = {
    };
    registry.register(checks, validator);
}

/**
 * Implementation of custom validations.
 */
export class JpipeHoverProvider implements HoverProvider{
    getHoverContent(document: LangiumDocument<AstNode>, params: HoverParams, cancelToken?: CancellationToken | undefined): MaybePromise<Hover | undefined> {
        throw new Error('Method not implemented.');
    }
}