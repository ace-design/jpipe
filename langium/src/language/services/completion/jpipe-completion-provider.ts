import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isSupport, isVariable } from "../../generated/ast.js";
import { stream } from "../../../../node_modules/langium/src/utils/stream.js"
import { SupportCompletionProvider } from "./support-completion.js";

export interface CompletionProvider<T>{
    getCandidates(potential_references: Stream<AstNodeDescription>, node: T): Set<AstNodeDescription>;
}

//provides additional completion support for the jpipe language
export class JpipeCompletionProvider extends DefaultCompletionProvider{

    //filters reference candidates for variables in support statements for autocompletion
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        let potential_references = this.scopeProvider.getScope(refInfo).getAllElements();

        let references = this.findKeywords(potential_references);

        let addtional_references: Set<AstNodeDescription> | undefined;
        //if the current context is of a supporting statement, determines which variables should appear for autocomplete
        if(isSupport(_context.node)){
            let completion_provider = new SupportCompletionProvider();
            addtional_references = completion_provider.getCandidates(potential_references, _context.node);
        }
        
        addtional_references?.forEach(variable =>{
            references.add(variable);
        });

        return stream(references);
    }

    //helper function for finding non-variable keywords
    private findKeywords(potential_references: Stream<AstNodeDescription>): Set<AstNodeDescription>{
        let keywords = new Set<AstNodeDescription>();

        potential_references.forEach(potential =>{
            if(!isVariable(potential.node)){
                keywords.add(potential);
            }
        });

        return keywords;
    }
}