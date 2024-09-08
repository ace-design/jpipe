import { LangiumDocument, URI } from "langium";
import { CodeAction, CodeActionKind, WorkspaceEdit, Diagnostic, TextEdit } from "vscode-languageserver";
import { AbsolutePath } from "./utilities/path-utilities.js";

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