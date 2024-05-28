import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isInstruction, isSupport } from "../generated/ast.js";
import { stream } from "../../../node_modules/langium/src/utils/stream.js"

export class JpipeCompletionProvider extends DefaultCompletionProvider{
    
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        
        let typeMap: Map<string, string[]> = new Map<string,string[]>();
        typeMap.set('evidence', ['strategy']);
        typeMap.set('strategy', ['sub-conclusion', 'conclusion']);
        typeMap.set('sub-conclusion', ['strategy', 'conclusion']);
        typeMap.set('conclusion', []);
        let allPotentials = this.scopeProvider.getScope(refInfo).getAllElements();
        let newPotentials:AstNodeDescription[] = [];
        if(isSupport(_context.node)){
            _context.node.supporter
        }
        allPotentials.forEach((potential) =>{
            if(isInstruction(potential.node)){
                let potentialKind = potential.node.kind;
                
            if(isSupport(_context.node)){
                console.log("potential: " + potential.node.name +"\tpotential kind: " + potentialKind + "\tfirst kind: " + _context.node.supporter.ref?.kind);
                
                if(_context.node.supporter.ref !== undefined){
                    let firstKind=_context.node.supporter.ref.kind;                    
                    let allowableTypes: string[] | undefined = [];
                    
                    allowableTypes=typeMap.get(firstKind);
                    allowableTypes?.forEach((e) => {console.log(e)});
                    if(allowableTypes?.includes(potentialKind)){
                        console.log("potential: " + potential.node.name +"\tpotential kind: " + potentialKind);
                        newPotentials.push(potential);
                    }                    
                }
            }
            }
            

        });
        return stream(newPotentials);
    }

}