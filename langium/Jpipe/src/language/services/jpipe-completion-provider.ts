import { AstNodeDescription, ReferenceInfo, Stream } from "langium";
import { CompletionContext, DefaultCompletionProvider } from "langium/lsp";
import { isSupport, isVariable } from "../generated/ast.js";
import { stream } from "../../../node_modules/langium/src/utils/stream.js"

export class JpipeCompletionProvider extends DefaultCompletionProvider{
    typeMap: Map<string, string[]> = new Map<string,string[]>([
        ['evidence', ['strategy']],
        ['strategy', ['sub-conclusion', 'conclusion']],
        ['sub-conclusion', ['strategy', 'conclusion']],
        ['conclusion', []] 
    ]);
    //needs refactoring
    protected override getReferenceCandidates(refInfo: ReferenceInfo, _context: CompletionContext): Stream<AstNodeDescription> {
        let allPotentials = this.scopeProvider.getScope(refInfo).getAllElements();
        let newPotentials:AstNodeDescription[] = [];
        
        let allVariables:AstNodeDescription[] = [];

        allPotentials.forEach((potential) =>{
            if(isVariable(potential.node)){
                console.log("pushing: " + potential.node.name + " to all variables");
                allVariables.push(potential);
            }else{
                newPotentials.push(potential);
            }
        });

        if(isSupport(_context.node)){
            console.log("context is support... getting types");
            if(_context.node.supporter.ref !== undefined){
                console.log("getting right element");
                this.getSupporteeVariables(allVariables,_context).forEach((v)=>{
                    newPotentials.push(v);
                });
            }else{
                /*
                console.log("getting left element");
                this.getSupporterVariables(allVariables,_context).forEach((v)=>{
                    newPotentials.push(v);
                });
                */
            }
        }
        return stream(newPotentials);
    }

    getSupporteeVariables(allVariables: AstNodeDescription[], _context: CompletionContext): AstNodeDescription[]{
        let supporteeVariables: AstNodeDescription[] = [];
        if(isSupport(_context.node)){
            if(_context.node.supporter.ref !== undefined){
                let supporter_kind = _context.node.supporter.ref.kind;
                let allowable_types = this.typeMap.get(supporter_kind);
                console.log("allowable types for: " + supporter_kind  + ":");
                allowable_types?.forEach((t)=> {
                    console.log("\t" + t);
                });
                allVariables.forEach((variable)=>{
                    if(isVariable(variable.node)){
                        let potential_kind = variable.node.kind;
                        console.log("\tsupporter kind: " +supporter_kind + "\tsupportee name: " + variable.node.name);
                        
                        if(allowable_types?.includes(potential_kind)){
                            console.log("pushing " + variable.node.name  +  " to supporteevariables");
                            supporteeVariables.push(variable);
                        } 
                    }
                });
            }
        }
        
        return supporteeVariables;
    }

    getSupporterVariables(allVariables: AstNodeDescription[], _context: CompletionContext): AstNodeDescription[]{
        let supporterVariables: AstNodeDescription[] = [];

        allVariables.forEach((variable) =>{
            if(isVariable(variable.node)){
                let variable_kind = variable.node.$type;
                let allowable_types = this.typeMap.get(variable_kind);

                allVariables.forEach((possible_variable)=>{
                    if(isVariable(possible_variable.node)){
                        if(allowable_types?.includes(possible_variable.node.kind)){
                            supporterVariables.push(possible_variable);
                        }
                    }
                });
            }
        });

        return supporterVariables;
    }

    stringer( arr: AstNodeDescription[]): string{
        let p = "[ ";
        arr.forEach((a) =>{
            if(isVariable(a.node)){
                p+=a.node.name;
                p+=", ";
            }
        });
        p+=" ]";
        return p;
    }
    isPossibleSupporter1( potential_kind: string, _context: CompletionContext): boolean{
        let supportee_kinds: string[] | undefined = this.typeMap.get(potential_kind);
        let isPossibleSupporter: boolean = false;
        console.log("\tsupporter kind: " + potential_kind +  "\tsupportee kind: " + supportee_kinds?.toString());
        supportee_kinds?.forEach((supportee_kind) =>{
            if(this.contextContains(supportee_kind, _context)){
                console.log("\tcontext contains " + supportee_kind + "For " + potential_kind)
                isPossibleSupporter = true;
            }
        });
        return isPossibleSupporter;
    }
    contextContains(variableType:string, _context: CompletionContext): boolean{
        let returnValue = false;
        _context.document.references.forEach((cross_ref) =>{
            console.log("\tchecking references: " + _context.node?.$type);
            let ref = cross_ref.ref;
            if(isVariable(ref)){
                console.log("\tchecking cross ref: " + ref.name + "for " + variableType);
                if(ref.kind === variableType){
                    console.log("\tbariable Type: "  + variableType + "\tref name: " + ref.name + "\t ref kind: " + ref.kind);
                    returnValue = true;
                }
            }
        });
        return returnValue;
    }

}