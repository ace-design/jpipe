import { type AstNode, type MaybePromise, } from 'langium';
import { AstNodeHoverProvider } from 'langium/lsp';
import { Hover } from 'vscode-languageserver';
import { isClassType, isInstructionType } from './validation/main-validation.js';
import { isVariable } from '../generated/ast.js';

//provides hover for variables and class types
export class JpipeHoverProvider extends AstNodeHoverProvider{
    protected getAstNodeHoverContent(node: AstNode): MaybePromise<Hover | undefined> {
        if(isInstructionType(node)){
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
        
        if(isClassType(node)){
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