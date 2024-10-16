import { AstNodeDescription, DefaultLinker, LinkingError, ReferenceInfo, Scope, Stream } from "langium";
import { JpipeAstType } from "../generated/ast.js";

//Linker which resolves references 
export class JpipeLinker extends DefaultLinker{
    
    //Links candidates to their references, with a special case being for load statements
    override getCandidate(refInfo: ReferenceInfo): AstNodeDescription | LinkingError {
        let description: AstNodeDescription | undefined = undefined;

        const scope = this.scopeProvider.getScope(refInfo);

        description = scope.getElement(this.getElementName(refInfo, scope));

        return description ?? this.createLinkingError(refInfo);
    }

    //helper function to get the searched for element name (added for future potential cases)
    private getElementName(refInfo: ReferenceInfo, scope: Scope): string{
        let element_name: string;

        if(refInfo.container.$type as keyof JpipeAstType === "Load"){
            let node = this.getEntryNode(scope.getAllElements());
            
            element_name = node.name;
        }else{
            element_name = refInfo.reference.$refText;
        }

        return element_name;
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