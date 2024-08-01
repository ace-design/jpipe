import { type Module, inject, } from 'langium';
import { createDefaultModule, createDefaultSharedModule, type DefaultSharedModuleContext, type LangiumServices, type LangiumSharedServices, type PartialLangiumServices } from 'langium/lsp';
import { JpipeGeneratedModule, JpipeGeneratedSharedModule } from './generated/module.js';
import { JpipeHoverProvider } from './services/jpipe-hover-provider.js';
import { JpipeCompletionProvider } from './services/completion/jpipe-completion-provider.js';
import { JpipeValidator, registerValidationChecks } from './services/validation/main-validation.js';
import { JpipeScopeProvider } from './services/jpipe-scope-provider.js';

/**
 * Declaration of custom services - add your own service classes here.
 */
export type JpipeAddedServices = {
    validation: {
        validator: JpipeValidator
    }
}

/**
 * Union of Langium default services and your custom services - use this as constructor parameter
 * of custom service classes.
 */
export type JpipeServices = LangiumServices & JpipeAddedServices

/**
 * Dependency injection module that overrides Langium default services and contributes the
 * declared custom services. The Langium defaults can be partially specified to override only
 * selected services, while the custom services must be fully specified.
 */
export const JpipeModule: Module<JpipeServices, PartialLangiumServices & JpipeAddedServices> = {
    validation: {
        validator: () => new JpipeValidator()
    },
    lsp:{
        CompletionProvider: (services) => new JpipeCompletionProvider(services),
        HoverProvider: (services) => new JpipeHoverProvider(services),
    },
     references:{
        ScopeProvider: (services) => new JpipeScopeProvider(services)
     }
};

/**
 * Create the full set of services required by Langium.
 *
 * First inject the shared services by merging two modules:
 *  - Langium default shared services
 *  - Services generated by langium-cli
 *
 * Then inject the language-specific services by merging three modules:
 *  - Langium default language-specific services
 *  - Services generated by langium-cli
 *  - Services specified in this file
 *
 * @param context Optional module context with the LSP connection
 * @returns An object wrapping the shared services and the language-specific services
 */
export function createJpipeServices(context: DefaultSharedModuleContext): {
    shared: LangiumSharedServices,
    Jpipe: JpipeServices
} 
{
    const shared = inject(
        createDefaultSharedModule(context),
        JpipeGeneratedSharedModule
    );
    const Jpipe = inject(
        createDefaultModule({ shared }),
        JpipeGeneratedModule,
        JpipeModule
    );
    shared.ServiceRegistry.register(Jpipe);
   
    registerValidationChecks(Jpipe);
    
    
    return { shared, Jpipe };
}