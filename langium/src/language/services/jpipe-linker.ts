import { AstNodeDescription, DefaultLinker, LinkingError, ReferenceInfo, Stream } from "langium";
import { JpipeAstType } from "../generated/ast.js";

export class JpipeLinker extends DefaultLinker{
    //Links candidates to their references, with a special case being for load statements
    override getCandidate(refInfo: ReferenceInfo): AstNodeDescription | LinkingError {
        const scope = this.scopeProvider.getScope(refInfo);
        let description: AstNodeDescription | undefined = undefined;
        
        if(refInfo.container.$type as keyof JpipeAstType === "Load"){
            let node = this.getEntryNode(scope.getAllElements());
            
            description = scope.getElement(node.name);
        }else{
            description = scope.getElement(refInfo.reference.$refText);
        }
        
        return description ?? this.createLinkingError(refInfo);
    }

    //Finds the first node which defines a diagram to link to
    private getEntryNode(node_stream: Stream<AstNodeDescription>): AstNodeDescription{
        let nodes = node_stream.toArray();

        let i = 0;
        let type = "";
        let node: AstNodeDescription = nodes[0];

        while(((i < 0) && this.typeIncorrect(type))){
            node = nodes[i];
            type = node.type;
        }

        return node;
    }

    //helper function to clarify code
    private typeIncorrect(type: string): boolean{
        return !(type === "JustificationPattern" || type === "Composition");
    }
}