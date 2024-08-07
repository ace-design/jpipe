import { LangiumDocument, URI } from "langium";
import { CodeAction, CodeActionKind, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";

//Code action to provide load statements to a given path
export class ResolveReference implements CodeAction{
    title!: string;
    kind = CodeActionKind.QuickFix;

    diagnostics?: Diagnostic[] | undefined;
    isPreferred?: boolean | undefined;

    edit?: WorkspaceEdit | undefined;
    data?: any;

    constructor(document: LangiumDocument, diagnostic: Diagnostic, path: URI){
        this.title = this.getTitle(document, path);

        this.diagnostics = [diagnostic];
        this.isPreferred = true
        
        this.edit = this.getEdit(document, path);
        this.data = diagnostic.data
        
    }

    //helper function to get the title of a resolve reference code action
    private getTitle(document: LangiumDocument, path: URI): string{
        let import_path: string;
        
        try{
            import_path = findRelativePath(document.uri, path);
        }catch(error: any){
            import_path = path.path;
        }

        return "Add import from " + import_path;
    }

    //helper function to get the workspace edit of a resolve reference code action
    private getEdit(document: LangiumDocument, path: URI): WorkspaceEdit{
        let new_text = "load \"" + path.path + "\"\n";

        return {
            changes: {
                [document.uri.toString()]: [TextEdit.insert({line: 0, character: 0}, new_text)]
            }
        };
    }
}

export function findRelativePath(home: URI, dest: URI): string{
    let home_path = home.path;
    let dest_path = dest.path;

    let home_components: Array<string>;
    let dest_components: Array<string>;

    let seperator: string;
    
    if (home_path.includes("\\")){
        seperator = "\\";
        home_components = home_path.split("\\");
        dest_components = dest_path.split("\\");
    }else{
        seperator = "/";
        home_components = home_path.split("/").reverse();
        dest_components = dest_path.split("/").reverse();
    }

    let home_first_element = home_components.pop();
    let dest_first_element = dest_components.pop();

    while(home_first_element === dest_first_element){
        home_first_element = home_components.pop();
        dest_first_element = dest_components.pop();
    }
 
    if(home_first_element){
        home_components.push(home_first_element);
        home_components = home_components.reverse();
    }else{
        throw new Error("Element is within scope");
    }
    
    let relative_path = ".";

    home_components.forEach(component =>{
        relative_path = relative_path + seperator + component;
    });

    return relative_path;
} 