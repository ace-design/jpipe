import { Range, Position, uinteger } from "vscode-languageserver/node.js";
//Contains various range utilities

//takes a range and turns it into a string
export function toString(range: Range | undefined): string{
    if(!range){
        return "";
    }
    return "([" + range.start.line + "-" + range.start.character + "], " + "[" + range.end.line + "-" + range.end.character + "])";
}

//Determines if the container range contains the contained range
export function contains(container: Range, contained: Range): boolean{
    let contains = false;

    if(compare(container.start, contained.start) <= 0){
        if(compare(container.end, contained.end)>=0){
            contains = true;
        }
    }

    return contains;
}

//returns 1 if p1>p2, returns -1 if p2<p1, returns 0 if p1=p2
export function compare(p1: Position, p2: Position): number{
    if(p1.line > p2.line){
        return 1;
    }else if(p1.line < p2.line){
        return -1;
    }else{
        if(p1.character > p2.character){
            return 1;
        }else if(p1.character < p2.character){
            return -1;
        }else{
            return 0;
        }
    }
}

//helper function to make a position
export function makePosition(line: uinteger, character: uinteger): Position{
    return {line: line, character: character};
}