import { type AstNode, type MaybePromise, } from 'langium';
import { AstNodeHoverProvider } from 'langium/lsp';
import { Hover } from 'vscode-languageserver';
import { isDeclaration, isInstruction } from '../generated/ast.js';

//provides hover for variables and class types
export class JpipeHoverProvider extends AstNodeHoverProvider{
    protected getAstNodeHoverContent(node: AstNode): MaybePromise<Hover | undefined> {
        if(isInstruction(node)){
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `${node.kind}: ${node.information.toString()}`
                }
            } 
        }
        
        if(isDeclaration(node)){
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