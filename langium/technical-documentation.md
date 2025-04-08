# Feature List

## Settings
- Checks Graphviz is installed on computer (Check Graphviz Installation)
- Checks Java is installed on computer (Check Java Installation)
- Enable/Disable automatic setting fixes (Developer Mode)
- Set up jar file used by compiler (jar file)
- Set log level used by compiler (Log level)

## Image Generation
- Download image (ImageGenerator + Context Manager)
- Display Image (PreviewProvider, ImageGenerator, HTMLProvider, CommandManager)
- GoToDefinition on Images (PreviewProvider, SymbolLocator)

## Completion
- Provides completion support (JpipeCompletionProvider, DeclarationCompletionProvider, SupportCompletionProvider)

## Validation
- Checks kinds of implemented classes (ImplementationValidator)
- Checks for conclusion in justification (JustificationValidator)
- Checks for conclusion and @support in pattern (PatternValidator)
- Checks for @support in justification (VariableValidator)
- Checks kinds of ____ supports ___ statements (SupportValidator)

## Code Actions (Quick fixes)
- Add variable definition to start of justification (AddVariableDefinitionLine)
- Change declaration type (ChangeDeclarationRegistrar)
- Removes implemented declaration (RemoveImplemented)
- Removes lines with errors (RemoveLine)
- Resolves references to implemented classes and adds a load statement line at top of file (ResolveReferenceRegistrar)

## Hovering
- Adds hover support (JpipeHoverProvider)

---

# Features and Relevant Files

## Extension: Settings

**src/extension/configuration/abstract-configuration.ts**  
**ConfigurationManager**
- Keeps track of and updates settings

**src/extension/managers/configuration-manager.ts**

**src/extension/environment-tests/abstract-environment.ts**  
**EnvironmentManager**
- Runs environment checks on startup

### Check Graphviz installation
- Checks if the user has Graphviz installed on their computer  
  - (configuration) `src/extension/configuration/check-graphviz.ts`
  - (event running) `src/extension/environment-tests/graphviz-environment-check.ts`

### Check Java installation
- Checks if the user has Java installed on their computer  
  - (configuration) `src/extension/configuration/check-java.ts`
  - (event running) `src/extension/environment-tests/java-environment-check.ts`

### Developer Mode
- Disable to remove automatic fixes for configurations  
  - `src/extension/configuration/developer-mode.ts`

### Jar file
- Allows user to set the jar file used by the compiler  
  - `src/extension/configuration/jar-file.ts`

### Log Level
- Allows user to set the log level displayed in jpipe_debug output channel  
  - `src/extension/configuration/log-level.ts`

---

## Extension: Image Generation

**PreviewProvider**
- Sets up the webview panel and manages all interactions with it  
  - `src/extension/image-generation/preview-provider.ts`

**ImageGenerator**
- Interacts with the compiler to return an image based on settings  
  - `src/extension/image-generation/image-generator.ts`

**HTMLProvider**
- Gives all HTML pages  
  - `src/extension/image-generation/html-provider.ts`

**SymbolLocator**
- Locates the range in a text file of where a node is located  
  - `src/extension/image-generation/symbol-locator.ts`

---

## Extension: Management

**ContextManager**
- Keeps track of and updates context keys including if cursor is inside a given justification or if cursor is at title of a justification (Used for downloading png of justification)  
  - `src/extension/managers/context-manager.ts`

**CommandManager**
- Registers commands with subscriptions so they can be run by other classes (Used for image generation)  
  - `src/extension/managers/command-manager.ts`

**EventManager**
- Notifies event subscribers when an event has occurred and runs their update function (used by context manager, configurations, and image generation)  
  - `src/extension/managers/event-manager.ts`

---

## Language Server: Completion

**JpipeCompletionProvider**
- Synchronizes individual completion providers and provides completion support  
  - `src/language/services/completion/jpipe-completion-provider.ts`

**DeclarationCompletionProvider**
- Provides completion support for references to declarations  
  - `src/language/services/completion/class-completion.ts`

**SupportCompletionProvider**
- Provides completion support for references to support statements  
  - `src/language/services/completion/support-completion.ts`

---

## Language Server: Validation

**JpipeValidationRegistrar**
- Registers individual validation checks  
  - `src/language/services/validation/main-validation.ts`

**Validator**
- Abstract class which automatically sets up validation checks  
  - `src/language/services/validation/validators/abstract-validator.ts`

**ImplementationValidator**
- Validates the kinds of classes which are implemented (ex. Doesn’t allow justifications to be implemented by other classes)  
  - `src/language/services/validation/validators/declaration-validator.ts`

**JustificationValidator**
- Validates that there is a conclusion in the justification  
  - `src/language/services/validation/validators/justification-validator.ts`

**PatternValidator**
- Checks if there is a conclusion in the pattern, checks if there is an @support variable in the pattern  
  - `src/language/services/validation/validators/pattern-validator.ts`

**SupportValidator**
- Validates the kinds of ___ supports ___ statements  
  - `src/language/services/validation/validators/support-validator.ts`

**VariableValidator**
- Checks if there is an @support in justification  
  - `src/language/services/validation/validators/variable-validator.ts`

---

## Language Server: Code Actions (Quick fixes)

**JpipeCodeActionProvider**
- Registers all code actions with their corresponding error code

**CodeActionRegistrar**
- Abstract Factory class for code actions  
  - `src/language/services/validation/code-actions/code-action-registration.ts`

**RegistrableCodeAction**
- Abstract class that defines a code action factory which is also the code action itself  
  - `src/language/services/validation/code-actions/code-action-registration.ts`

**AddVariableDefinitionLine**
- Adds a variable definition to the line directly following the start of the declaration  
  - `src/language/services/validation/code-actions/add-line/add-variable-definition.ts`  
  - `src/language/services/validation/code-actions/add-line/abstract-add-line.ts`

**ChangeDeclarationRegistrar**
- Changes the declaration type given various error codes  
  - `src/language/services/validation/code-actions/change-declaration.ts`

**RemoveImplemented**
- Removes the implemented declaration  
  - `src/language/services/validation/code-actions/remove-implemented.ts`

**RemoveLine**
- Removes a line  
  - `src/language/services/validation/code-actions/remove-line.ts`

**ResolveReferenceRegistrar**
- Resolves a reference to an implemented declaration and adds the load statement to the top of the file  
  - `src/language/services/validation/code-actions/resolve-reference.ts`  
  - `src/language/services/jpipe-scope-computation.ts`  
  - `src/language/services/jpipe-scope-provider.ts`  
  - `src/language/services/jpipe-linker.ts`

---

## Extension: Hovering

**JpipeHoverProvider**
- Provides hover support over variables  
  - `src/language/services/jpipe-hover-provider.ts`
