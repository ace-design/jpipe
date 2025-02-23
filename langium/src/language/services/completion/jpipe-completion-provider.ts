import { AstNodeDescription, ReferenceInfo, Stream, stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isDeclaration, isCompositionInformation, isSupport } from "../../generated/ast.js";
import { SupportCompletionProvider } from "./support-completion.js";
import { DeclarationCompletionProvider } from "./class-completion.js";

//implement interface when adding new complettion provider
export interface JpipeCompletion{
    getCandidates(potential_references: Set<AstNodeDescription>, refInfo: ReferenceInfo): Set<AstNodeDescription>;
}

//provides additional completion support for the jpipe language
export class JpipeCompletionProvider extends DefaultCompletionProvider{

    //filters reference candidates for variables in support statements for autocompletion
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        let completion_provider: JpipeCompletion | undefined;
        let addtional_references: Set<AstNodeDescription> | undefined;

        let references = new Set<AstNodeDescription>();

        let potential_references = this.scopeProvider.getScope(refInfo).getAllElements().toSet();

        if(isSupport(_context.node)){
            //if the current context is of a supporting statement, determines which variables should appear for autocomplete
            completion_provider = new SupportCompletionProvider();
        }else if(isDeclaration(_context.node) || isCompositionInformation(_context.node)){
            //provides completion for class references
            completion_provider = new DeclarationCompletionProvider()
        }

        addtional_references = completion_provider?.getCandidates(potential_references, refInfo);
        
        addtional_references?.forEach(variable =>{
            references.add(variable);
        });

        return stream(references);
    }

    // //helper function for finding non-variable keywords
    // private findKeywords(potential_references: Set<AstNodeDescription>): Set<AstNodeDescription>{
    //     let keywords = new Set<AstNodeDescription>();

    //     potential_references.forEach(potential =>{
    //         if(!isVariable(potential.node)){
    //             keywords.add(potential);
    //         }
    //     });

    //     return keywords;
    // }
}