import { type AstNode, type MaybePromise, } from 'langium';

import { AstNodeHoverProvider } from 'langium/lsp';
import { Hover } from 'vscode-languageserver';
import { isClass, isSupportingStatement } from '../generated/ast.js';



/**
 * Implementation of custom validations.
 */
export class JpipeHoverProvider extends AstNodeHoverProvider{
    protected getAstNodeHoverContent(node: AstNode): MaybePromise<Hover | undefined> {
        if(isSupportingStatement(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `description: ${node.supporter.ref?.information}`
                }
            }           
        }
        
        if(isSupportingStatement(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `${node.supporter.ref?.information} ${node.supporter.ref?.information}`
                }
            }

        }
        
        if(isClass(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `class type: ${node.type}`
                }
            } 
        }
        
        return undefined;     
    }
}
