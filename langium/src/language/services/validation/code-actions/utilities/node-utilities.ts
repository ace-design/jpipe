import { AstNodeDescription, PrecomputedScopes } from "langium";
import { Range, integer } from "vscode-languageserver";
import { contains } from "./range-utilities.js";

//custom type to store map entries
type MapEntry<K,T> = {
    key?: K
    value: T
}

//finds the smallest node which contains the given range or returns undefined if no node contains the given range
export function findNode(type: string, range: Range, scopes: PrecomputedScopes | undefined): AstNodeDescription | undefined{
    let smallest_container: MapEntry<AstNodeDescription, Range> | undefined;

    scopes?.forEach((description) =>{
        if(description.selectionSegment){
            if(contains(description.selectionSegment.range, range)){
                if(description.type === type){
                    smallest_container = getSmallestContainer(smallest_container, {key: description, value: description.selectionSegment.range});
                }
            }
        }
    });

    if(smallest_container){
        return smallest_container.key;
    }else{
        return smallest_container;
    }
}

//helper function to find the smallest node which contains a given range
export function getAnyNode(range: Range, scopes: PrecomputedScopes | undefined): AstNodeDescription {        
    let smallest_container: MapEntry<AstNodeDescription, Range> = {
        value: {start: {line: 0, character: 0}, end: {line: integer.MAX_VALUE, character: integer.MAX_VALUE} },
    }

    scopes?.forEach((description) =>{
        if(description.selectionSegment){
            if(contains(description.selectionSegment.range, range)){
                smallest_container = getSmallestContainer(smallest_container, {key: description, value: description.selectionSegment.range});
            }
        }
    });

    if(smallest_container.key){
        return smallest_container.key;
    }else{
        throw new Error("Smallest container cannot be found");
    }

}

//helper function to determine which container is the smallest between a current and a new range
function getSmallestContainer(current_smallest: MapEntry<AstNodeDescription, Range> | undefined, new_comparison: MapEntry<AstNodeDescription, Range>): MapEntry<AstNodeDescription, Range>{
    let new_smallest: MapEntry<AstNodeDescription, Range>;
    
    if(!current_smallest){
        new_smallest = new_comparison;
    }else{
        let smaller_than_smallest = contains(current_smallest.value, new_comparison.value);

        if(smaller_than_smallest){
            new_smallest = new_comparison;
        }else{
            new_smallest = current_smallest;
        }
    }


    return new_smallest;
}