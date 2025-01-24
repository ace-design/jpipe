import { LangiumDocument } from "langium";
import { CodeAction, CodeActionKind, Diagnostic, WorkspaceEdit, TextEdit, CodeActionParams } from "vscode-languageserver";
import { RegistrableCodeAction } from "../code-action-registration.js";

//Class that adds a conclusion into the first line of the diagram selected
export abstract class AbstractAddLine extends RegistrableCodeAction{
    public kind = CodeActionKind.QuickFix;

    public diagnostics?: Diagnostic[];
    public isPreferred?: boolean;

    public edit?: WorkspaceEdit;
    public data: any;

    public constructor(code: string){
        super(code);
    }

    //returns the associated code action given diagnostic information
    protected override getAction(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic): CodeAction {
        this.diagnostics = [diagnostic];
        this.isPreferred = true;

        this.edit = this.getEdit(document, diagnostic);
        this.data = diagnostic.data;

        return this;
    }

    //helper function to determine the edit provided
    protected getEdit(document: LangiumDocument, diagnostic: Diagnostic): WorkspaceEdit {
        return {
            changes: {
                [document.uri.toString()]: [TextEdit.insert({line: diagnostic.range.start.line+ 1, character: 0}, "\t" + this.getNewLineText() + "\n")]
            }
        }
    }

    protected abstract getNewLineText(): string;
}