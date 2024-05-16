import type { ValidationChecks } from 'langium';
import type { JpipeAstType} from '../generated/ast.js';
import type { JpipeServices } from '../jpipe-module.js';

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
export class JpipeValidator {

}
