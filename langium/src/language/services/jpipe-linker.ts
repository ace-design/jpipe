import { AstNode, AstNodeDescription, DefaultLinker, isLinkingError, LangiumDocument, LinkingError, Reference, ReferenceInfo } from "langium";
import { JpipeAstType } from "../generated/ast.js";
import { getModelNode } from "./index.js";
import { AbsolutePath, Path, RelativePath } from "./validation/code-actions/utilities/path-utilities.js";

export class JpipeLinker extends DefaultLinker{

    override getCandidate(refInfo: ReferenceInfo): AstNodeDescription | LinkingError {
        const scope = this.scopeProvider.getScope(refInfo);
        let description: AstNodeDescription | undefined = undefined;
        
        if(refInfo.container.$type as keyof JpipeAstType === "Load"){
            let elements = scope.getAllElements();
            let load_path = this.getLoadPath(refInfo);

            //ONLY FOR DEBUG
            console.log("\n**********************");

            let model = getModelNode(refInfo.container);
            let document_uri = model ? model.$document ? model.$document.uri.fsPath : undefined: undefined;

            document_uri ? console.log("Document URI: " + document_uri) : console.log("No document URI found");
            console.log("Ref text: " + refInfo.reference.$refText);
            load_path ? console.log("Absolute URI (calculated): " + load_path.toString()) : console.log("No Absolute URI calculated");

            console.log("ELements length: " + elements.count());
            //

            if(load_path){
                elements.forEach(element => {
                    console.log("element found")
                    let e_uri = element.documentUri.fsPath;
                    if((new AbsolutePath(e_uri)).toString() == load_path?.toString()){
                        console.log("same uri found: " + element.name);
                    }
                });
            }



        }else{
            description = scope.getElement(refInfo.reference.$refText);
        }
        
        return description ?? this.createLinkingError(refInfo);
    }

    
    private getLoadPath(refInfo: ReferenceInfo): AbsolutePath | undefined{
        let load_path: AbsolutePath | undefined = undefined;

        let load_text = refInfo.reference.$refText;

        let model = getModelNode(refInfo.container);
        let document_uri = model ? model.$document ? model.$document.uri.fsPath : undefined : undefined;
        
        if(Path.isAbsoluteString(load_text)){
            load_path = document_uri ? (new AbsolutePath(load_text)) : undefined;
        }else{
            load_path = document_uri ? (new RelativePath(load_text)).toAbsolutePath(new AbsolutePath(document_uri)) : undefined;
        }
        
        return load_path;
    }
}