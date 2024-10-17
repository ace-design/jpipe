import { AstUtils, IndexManager, LangiumDocument, LinkingErrorData, URI } from "langium";
import { CodeAction, CodeActionKind, WorkspaceEdit, Diagnostic, TextEdit, CodeActionParams } from "vscode-languageserver";
import { AbsolutePath } from "./utilities/path-utilities.js";
import { CodeActionRegistrar } from "./code-action-registration.js";
import { LangiumServices } from "langium/lsp";


export class ResolveReferenceRegistrar extends CodeActionRegistrar{
    private readonly index_manager: IndexManager;

    constructor(services: LangiumServices, code: string){
        super(services, code)

        this.index_manager = services.shared.workspace.IndexManager;
    }

    protected override getActions(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): Array<CodeAction> {
        console.log("linking error found");
        let code_actions = new Array<CodeAction>();
        
        let data = this.toLinkingError(diagnostic.data);
        let paths = this.getPaths(document, data);
        console.log("Path founds")

        paths.forEach(path => {
            code_actions.push(new ResolveReference(document, diagnostic, path))
        });

        return code_actions;        
    }

    private toLinkingError(data: any): LinkingErrorData{
        if(data.code && data.containerType && data.property && data.refText){
            return {
                code: data.code,
                containerType: data.containerType,
                property: data.property,
                refText: data.refText,
                actionSegment: data.actionSegment,
                actionRange: data.actionRange
            }
        }else{
            throw new Error("Diagnostic data not in correct form");
        }
    }

    private getPaths(document: LangiumDocument, data: LinkingErrorData): Set<URI>{
        let paths = new Set<URI>();
        
        this.index_manager.allElements().forEach(e => {
            console.log("elements found")
            if(e.name === data.refText){
                if(e.node){
                    let home_doc = AstUtils.getDocument(e.node);

                    paths.add(home_doc.uri);
                }
            }
        });

        return paths;
    }
}


//Code action to provide load statements to a given path
export class ResolveReference implements CodeAction{
    public title!: string;
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[] | undefined;
    public isPreferred?: boolean | undefined;

    public edit?: WorkspaceEdit | undefined;
    public data?: any;

    public constructor(document: LangiumDocument, diagnostic: Diagnostic, path: URI){
        let relative_path = this.getRelativePath(document.uri, path);
        this.title = "Add import from " + relative_path;

        this.diagnostics = [diagnostic];
        this.isPreferred = true
        
        this.edit = this.getEdit(document, relative_path);
        this.data = diagnostic.data
        
    }

    //helper function to get the relative path
    private getRelativePath(home_URI: URI, dest_URI: URI): string{
        let import_path: string;
        let home_path = new AbsolutePath(home_URI);

        try{
            import_path = home_path.getRelativePathTo(dest_URI.path).toString();
        }catch(error: any){
            console.log(error.toString());
            import_path = dest_URI.path;
        }

        return import_path;
    }

    //helper function to get the workspace edit of a resolve reference code action
    private getEdit(document: LangiumDocument, relative_path: string): WorkspaceEdit{
        let new_text = "load \"" + relative_path + "\"\n";

        return {
            changes: {
                [document.uri.toString()]: [TextEdit.insert({line: 0, character: 0}, new_text)]
            }
        };
    }
}