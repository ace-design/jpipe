import { AstNode, DefaultScopeProvider, LangiumCompletionParser, LangiumCoreServices, LangiumDocuments, MapScope, ReferenceInfo, Scope, URI } from "langium";
import { Class, isModel, Load, Model } from "../generated/ast.js";

export class JpipeScopeProvider extends DefaultScopeProvider{
    private langiumDocuments: () => LangiumDocuments;

    constructor(services: LangiumCoreServices){
        super(services)
        this.langiumDocuments = () => services.shared.workspace.LangiumDocuments;
    }
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

    //use complicated recursion to get all import statements referenced by the document
    private getImports(document_imports: Set<URI>, all_imports: Set<URI>, doc_provider: LangiumDocuments): Set<URI>{
        document_imports.forEach((doc_import) =>{
            let load_statements = this.getLoadStatements(doc_import, doc_provider);

            if(load_statements.length !== 0){
                let new_document_uris = this.getURIs(load_statements);
                this.getImports(new_document_uris, all_imports);
            }
        });
    }
//use complicated recursion to get all import statements referenced by the document
private getImports(current_URI: URI, all_imports: Set<URI>, doc_provider: LangiumDocuments): Set<URI>{
    if(all_imports.has(current_URI)){
        return new Set<URI>();
    }else{
        all_imports.add(current_URI);

        this.setImports(current_URI, doc_provider);


        let load_URIs = this.uri_map.get(current_URI);
    
        if(load_URIs){
            load_URIs.forEach(load_URI =>{
                this.getImports(load_URI, all_imports, doc_provider);
            });
        }
        return all_imports;
    }
}


private setImports(current_URI: URI, doc_provider: LangiumDocuments): void{

    let load_URIs = getURIs(this.getNode(current_URI));

    this.uri_map.set(current_URI, load_URIs);

    load_URIs.forEach(load_URI =>{
        this.setImports(load_URI, doc_provider);
    });
}


private getNode(doc_import: URI): AstNode{
    let model = this.indexManager.allElements("Class", new Set<string>([doc_import.toString()]));
    let head = model.head();

    if(head){
        if(head.node){
            return head.node;
        }else{
            throw new Error("Node not found");
        }
    }else{
        throw new Error("Node not found");
    }
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