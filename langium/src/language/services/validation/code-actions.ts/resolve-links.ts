import { LangiumDocument, LinkingErrorData } from "langium";
import { CodeAction, CodeActionKind, WorkspaceEdit, Diagnostic, CodeActionParams } from "vscode-languageserver";

export class ResolveReference implements CodeAction{
    title!: string;
    kind = CodeActionKind.QuickFix;

    diagnostics?: Diagnostic[] | undefined;
    isPreferred?: boolean | undefined;

    edit?: WorkspaceEdit | undefined;
    data?: any;

    constructor(document: LangiumDocument, params: CodeActionParams, diagnostic: Diagnostic){
        let data = this.toLinkingError(diagnostic.data);
        data;
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

}
