import { AstNode, AstNodeDescription, DefaultScopeComputation, LangiumDocument, Cancellation } from "langium";
import { isJustificationPattern } from "../generated/ast.js";

//Cl
export class JpipeScopeComputation extends DefaultScopeComputation{

    override async computeExports(document: LangiumDocument, cancelToken = Cancellation.CancellationToken.None): Promise<AstNodeDescription[]> {
        let parent_node = document.parseResult.value;

        let children = (parent_node: AstNode) => {
            let children_list = new Set<AstNode>();

            if(parent_node.$cstNode){
                parent_node.$cstNode.root.content.forEach(cstnode =>{
                    children_list.add(cstnode.astNode);
                    if(isJustificationPattern(cstnode.astNode)){
                        cstnode.astNode.variables.forEach(v =>{
                            children_list.add(v);
                        })
                    }
                })
            }

            return children_list;
        }

        return this.computeExportsForNode(parent_node, document, children, cancelToken);
    }
}