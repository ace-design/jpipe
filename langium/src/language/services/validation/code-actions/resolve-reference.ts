import { LangiumDocument, URI } from "langium";
import { CodeAction, CodeActionKind, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";
import { FilePath } from "./path-utilities.js";

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
        let home_path = new FilePath(document.uri);

        try{
            import_path = home_path.getRelativePathTo(path.path).toString();
        }catch(error: any){
            console.log(error.toString());
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