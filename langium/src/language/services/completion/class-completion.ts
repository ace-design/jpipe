import { AstNodeDescription, ReferenceInfo } from "langium";
import { JpipeCompletion } from "./jpipe-completion-provider.js";
import { isDeclaration } from "../../generated/ast.js";

//Provides completion for class references
export class DeclarationCompletionProvider implements JpipeCompletion{
    //provides reference candidates
    getCandidates(potential_references: Set<AstNodeDescription>, refInfo: ReferenceInfo): Set<AstNodeDescription> {
        let candidates = new Set<AstNodeDescription>(); 
        let verification_function: ((potential_references: Set<AstNodeDescription>) => Set<AstNodeDescription>) | undefined;

        if(refInfo.property === "implemented"){
            verification_function = (potential_references: Set<AstNodeDescription>) => this.filterReferencesByKind("pattern", potential_references);
        }else if(refInfo.property === "left" || refInfo.property === "right"){
            verification_function = (potential_references: Set<AstNodeDescription>) => this.filterReferencesByKind("justification", potential_references);
        }
        
        if(verification_function){
            candidates = verification_function.call(this, potential_references);
        }

        return candidates;
    }

    //helper function to filter class references by kind out of a list of nodes
    private filterReferencesByKind(property: string, potential_references: Set<AstNodeDescription>): Set<AstNodeDescription>{
        let candidates = new Set<AstNodeDescription>(); 

        potential_references.forEach(ref =>{
            if(isDeclaration(ref.node)){
                if(ref.node.kind === property){
                    candidates.add(ref);
                }
            }
        });

        return candidates;
    }
}