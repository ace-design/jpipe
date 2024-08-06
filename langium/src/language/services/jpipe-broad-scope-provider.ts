import { AstNode, AstNodeDescription, DefaultScopeProvider, LangiumCoreServices, ReferenceInfo, Scope, stream, Stream } from "langium";
import { getDocument } from "/home/braunc8/summer/langium/jpipegit/jpipe/langium/node_modules/langium/lib/utils/ast-utils.js"

export class JpipeBroadScopeProvider extends DefaultScopeProvider{
    constructor(services: LangiumCoreServices) {
        super(services);
    }

    override getScope(context: ReferenceInfo): Scope {
        const scopes: Array<Stream<AstNodeDescription>> = [];
        const referenceType = this.reflection.getReferenceType(context);

        const precomputed = getDocument(context.container).precomputedScopes;
        
        if (precomputed) {
            let currentNode: AstNode | undefined = context.container;
            do {
                const allDescriptions = precomputed.get(currentNode);
                if (allDescriptions.length > 0) {
                    scopes.push(stream(allDescriptions).filter(
                        desc => this.reflection.isSubtype(desc.type, referenceType)));
                }
                currentNode = currentNode.$container;
            } while (currentNode);
        }

        let result: Scope = this.getGlobalScope(referenceType, context);
        for (let i = scopes.length - 1; i >= 0; i--) {
            result = this.createScope(scopes[i], result);
        }
        return result;
    }
}

export interface JpipeScope {
    getElements(name: string): AstNodeDescription | undefined;

    getAllElements(): Stream<AstNodeDescription>;
}