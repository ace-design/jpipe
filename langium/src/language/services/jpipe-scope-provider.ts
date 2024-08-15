import { AstNode, DefaultScopeProvider, LangiumCoreServices, LangiumDocuments, MapScope, ReferenceInfo, Scope, URI } from "langium";
import { Declaration, isDeclaration, isModel, Load, Model } from "../generated/ast.js";
import { AbsolutePath, Path, RelativePath } from "./validation/code-actions/path-utilities.js";

export class JpipeScopeProvider extends DefaultScopeProvider{
    private langiumDocuments: () => LangiumDocuments;

    constructor(services: LangiumCoreServices){
        super(services)
        this.langiumDocuments = () => services.shared.workspace.LangiumDocuments;
    }

    //gets the global scope for a given reference
    protected override getGlobalScope(referenceType: string, _context: ReferenceInfo): Scope {
        let included_URIs: Set<URI> = new Set<URI>();

        let current_URI = getCurrentURI(_context.container);

        if(current_URI){
            included_URIs = this.getImports(current_URI, new Set<URI>(), this.langiumDocuments());
        }
        

        return this.globalScopeCache.get(referenceType, () => new MapScope(this.indexManager.allElements(referenceType, toString(included_URIs))));
    }

    //gets all imports from the current document recursively
    private getImports(current_URI: URI, all_imports: Set<URI>, doc_provider: LangiumDocuments): Set<URI>{
        if(all_imports.has(current_URI)){
            return new Set<URI>();
        }else{
            let load_URIs: Set<URI> | undefined;
            let node = this.getNode(current_URI);            
            
            all_imports.add(current_URI);        
            
            if(node){
                load_URIs = getURIs(node, current_URI);
            }
        
            if(load_URIs){
                load_URIs.forEach(load_URI =>{
                    this.getImports(load_URI, all_imports, doc_provider);
                });
            }
            return all_imports;
        }
    }

    //gets any node from a uri
    private getNode(doc_import: URI): AstNode | undefined{
        let node: AstNode | undefined;
        let model = this.indexManager.allElements(undefined, new Set<string>([doc_import.toString()]));
        let head = model.head();

        if(head){
            if(head.node){
                node = head.node;
            }
        }

        return node;
    }
}

//turns a set of URIs into a set of strings
function toString(URIs: Set<URI>): Set<string>{
    let strings = new Set<string>();

    URIs.forEach(uri =>{
        strings.add(uri.toString());
    });

    return strings;
}

//gets the current uri from a given node
function getCurrentURI(node: AstNode): URI | undefined{
    let current_URI: URI | undefined;
    let model = getModelNode(node);
    
    if(model){
        if(model.$document){
            current_URI = model.$document.uri;
        }
    }

    return current_URI;
}

//gets all URIs within a certain document from a node
function getURIs(node: AstNode, current_URI: URI): Set<URI>{
    let links = new Set<URI>;

    let load_statements = findLoadStatements(node);

    load_statements.forEach(load_statement => {
        console.log("link: " + load_statement.name);
        let uri: URI;
        //add in path resolution here
        if(Path.isAbsoluteString(load_statement.name)){
            uri = URI.file(load_statement.name);

            console.log("\tis absolute");
            console.log("\turi found: " + uri.toString());
        }else{
            let relative_path = new RelativePath(load_statement.name);
            let home_absolute = new AbsolutePath(current_URI);

            uri = URI.file(relative_path.toAbsolutePath(home_absolute).toString());
            console.log("\tis relative");
            console.log("\turi found: " + uri.toString());
            console.log("\trelative found: " + relative_path.toString());
        }

        
        links.add(uri);
    });

    return links;
}

//finds all load statements in a document from a node
function findLoadStatements(node: AstNode): Set<Load>{
    let load_statements = new Set<Load>();
    let model = getModelNode(node);

    if(model){
        model.loads.forEach(load_statement =>{
            load_statements.add(load_statement);
        });
    }

    return load_statements;
}

//gets the model node of a given node
export function getModelNode(node: AstNode): Model | undefined{
    let container = node.$container;
    while(container !== undefined && !isModel(container)){
        container = container.$container;
    }
    
    if(isModel(container)){
        return container;
    }else{
        return undefined;
    }
}

export function getDeclaration(node: AstNode): Declaration{
    let container: AstNode | undefined = node;
    while(container !== undefined && !isDeclaration(container)){
        container = container.$container;
    }
    
    if(isDeclaration(container)){
        return container;
    }else{
        throw new Error("Declaration not found");
    }
}