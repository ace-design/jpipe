import { type AstNode, type MaybePromise, } from 'langium';

import { AstNodeHoverProvider } from 'langium/lsp';
import { Hover } from 'vscode-languageserver';
import { isJustification, isInstruction } from '../generated/ast.js';



/**
 * Implementation of custom validations.
 */
export class JpipeHoverProvider extends AstNodeHoverProvider{
    protected getAstNodeHoverContent(node: AstNode): MaybePromise<Hover | undefined> {
        if(isInstruction(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `${node.kind}: ${node.information}`
                }
            } 
        }
        
        if(isJustification(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `class type: ${node.kind}`
                }
            } 
        }
        
        return undefined;     
    }
}
