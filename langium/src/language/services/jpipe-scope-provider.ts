import { AstNode, DefaultScopeProvider, MapScope, ReferenceInfo, Scope, URI } from "langium";
import { Class, isModel, Load, Model } from "../generated/ast.js";

export class JpipeScopeProvider extends DefaultScopeProvider{
    protected override getGlobalScope(referenceType: string, _context: ReferenceInfo): Scope {
        let included_uris: Set<string> = new Set<string>();

        let load_statements = this.findLoadStatements(_context.container);

        load_statements.forEach(load_statement =>{
            let uri = URI.file(load_statement.name);
            included_uris.add(uri.toString());
        });

        return this.globalScopeCache.get(referenceType, () => new MapScope(this.indexManager.allElements(referenceType, included_uris)));
    }

    //load statement type is preserved to implement loading of specific classes in the future
    private findLoadStatements(node: AstNode): Load[]{
        let load_statements: Load[] = [];
        let model = getModelNode(node);
        
        model.loads.forEach(load_statement =>{
            console.log(load_statement.name);
            load_statements.push(load_statement);
        });
        
        return load_statements;
    }
}

function getModelNode(node: AstNode): Model{
    let container = node.$container;
    while(container !== undefined && !isModel(container)){
        container = container.$container;
    }
    
    if(isModel(container)){
        return container;
    }else{
        throw new Error("Node has no Model");
    }
}

type LoadStatement = {
    link: string
}

function isModelNode(node: AstNode | undefined): node is Model{
    let is_model_node: boolean;

    if(node !== undefined){
        if(node.$type === 'Model'){
            is_model_node = true;
        }else{
            is_model_node = false;
        }
    }else{
        is_model_node = false;
    }

    return is_model_node;
}
function isClassNode(node: AstNode | undefined): node is Class{
    let is_class_node: boolean;

    if(node !== undefined){
        if(node.$type === 'Class' || node.$type === 'Composition' || node.$type === 'JustificationPattern'){
            is_class_node = true;
        }else{
            is_class_node = false;
        }
    }else{
        is_class_node = false;
    }

    return is_class_node;
}