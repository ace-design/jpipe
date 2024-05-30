
import { DefaultDefinitionProvider } from 'langium/lsp';
/*
import { findDeclarationNodeAtOffset } from 'langium/utils/cst-utils.js';
import { LangiumDocument, MaybePromise, } from 'langium';
import { CancellationToken, DefinitionParams, LocationLink } from 'vscode-languageserver';
*/
export class JpipeGoToProvider extends DefaultDefinitionProvider{
    /*
    override getDefinition(document: LangiumDocument, params: DefinitionParams, cancelToken?: CancellationToken): MaybePromise<LocationLink[] | undefined>{
        let loc: LocationLink[] = [];
        const rootNode = document.parseResult.value;

        return loc;
    }
    */
}