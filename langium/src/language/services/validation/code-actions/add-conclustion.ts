import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, Diagnostic, WorkspaceEdit, TextEdit, CodeActionParams } from "vscode-languageserver";
import { RegistrableCodeAction } from "./code-action-registration.js";
import { LangiumServices } from "langium/lsp";


export class AddConclusion extends RegistrableCodeAction{

    public title = "Add conclusion";
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[];
    public isPreferred?: boolean;

    public edit?: WorkspaceEdit;
    public data: any;

    public constructor(services: LangiumServices, code: string){
        super(services, code);
    }

    protected override getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction {
        this.diagnostics = [diagnostic];
        this.isPreferred = true;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data;

        return this;
    }

    private getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit {
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.insert({line: diagnostic.range.start.line+ 1, character: 0}, "\tconclusion c is \"\"\n\n")]
            }
        }
    }
}