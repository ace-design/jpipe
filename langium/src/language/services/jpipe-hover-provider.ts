import { type AstNode, type MaybePromise, } from 'langium';
import { AstNodeHoverProvider } from 'langium/lsp';
import { Hover } from 'vscode-languageserver';
import { isClass, isInstruction, isVariable } from './jpipe-completion-provider.js';


//provides hover for variables and class types
export class JpipeHoverProvider extends AstNodeHoverProvider{
    protected getAstNodeHoverContent(node: AstNode): MaybePromise<Hover | undefined> {
        if(isInstruction(node)){
            node.$cstNode
        }
        if(isVariable(node)){
            console.log('node');
            return {
                contents: {
                    kind: 'markdown',
                    language: 'Jpipe',
                    value: `${node.kind}: ${node.information.toString()}`
                }
            } 
        }
        
        if(isClass(node)){
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
