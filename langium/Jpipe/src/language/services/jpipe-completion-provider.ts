import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isSupport, isVariable } from "../generated/ast.js";
import { stream } from "../../../node_modules/langium/src/utils/stream.js"

export class JpipeCompletionProvider extends DefaultCompletionProvider{
    
    //needs refactoring
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        let typeMap: Map<string, string[]> = new Map<string,string[]>();
        typeMap.set('evidence', ['strategy']);
        typeMap.set('strategy', ['sub-conclusion', 'conclusion']);
        typeMap.set('sub-conclusion', ['strategy', 'conclusion']);
        typeMap.set('conclusion', []);
        
        let allPotentials = this.scopeProvider.getScope(refInfo).getAllElements();
        let newPotentials:AstNodeDescription[] = [];

        allPotentials.forEach((potential) =>{
            if(isSupport(_context.node) && isVariable(potential.node)){
                if(_context.node.supporter.ref !== undefined){
                    let potential_kind = potential.node.kind;
                    let supporter_kind = _context.node.supporter.ref.kind;                    
                    let allowable_types = typeMap.get(supporter_kind);
                    if(allowable_types?.includes(potential_kind)){
                        newPotentials.push(potential);
                    }                    
                }else{
                    newPotentials.push(potential);
                }
            }
        });
        return stream(newPotentials);
    }

}