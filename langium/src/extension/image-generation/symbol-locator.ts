import * as vscode from "vscode";
import { Position, Range } from "vscode-languageserver-types";
import { DefinitionRequest, LanguageClient } from "vscode-languageclient/node.js";
import { TextDocumentPositionParams } from "vscode-languageserver";
import { JPipeOutput, OutputManager } from "../managers/index.js";
import { rangeToString } from "langium/test";


export class SymbolLocator{
    private document!: vscode.TextDocument;
    private client: LanguageClient;

    constructor(client: LanguageClient, private readonly output_manager: OutputManager){
        this.client = client;
    }

    public updateDocument(document: vscode.TextDocument){
        this.document = document
    }

    public processMessage(message:any){
        this.output_manager.log(JPipeOutput.DEBUG, "beginning processing message");

        let text = message.text;
        let instruction_array: Array<string> = text.split("\n");

        let instruction_name = instruction_array[1];
        let instruction_text = instruction_array[3];

        this.output_manager.log(JPipeOutput.DEBUG, "instruction name: " + instruction_name + "\tinstruction text: " + instruction_text);
        
        let range = this.getSearchRange();

        this.output_manager.log(JPipeOutput.DEBUG, "Search range for entire document found");

        let instruction_name_location = this.findInstructionNameLocation(instruction_name, instruction_text, range);
       
        this.output_manager.log(JPipeOutput.DEBUG, "instruction name location found:")
        if(instruction_name_location){
            this.output_manager.log(JPipeOutput.DEBUG, "instruction name location: " + rangeToString(instruction_name_location))
            
            let params = this.createDefinitionParams(instruction_name_location.start)
            
            this.client.sendRequest(DefinitionRequest.type, params)  
            this.output_manager.log(JPipeOutput.DEBUG, "client request sent?")
        }
    }

    private findInstructionNameLocation(instruction_name: string, text: string, range: Range): Range | undefined{
        let instruction_name_location: Range | undefined = undefined;
        let instruction_text_location = this.locateText(text, range);
        
        if(instruction_text_location){
            let instruction_text_line = this.getLine(instruction_text_location);
            let instruction_name_location1 = this.locateText(" "+instruction_name+" ", instruction_text_line)
            
            if(instruction_name_location1){
                let iname_start_pos = Position.create(instruction_name_location1.start.line, instruction_name_location1.start.character+1)
                let iname_end_pos = Position.create(instruction_name_location1.end.line, instruction_name_location1.end.character-1)

                instruction_name_location =  Range.create(iname_start_pos, iname_end_pos);
            }
        }
        return instruction_name_location;
    }

    //helper function to create the parameters
    private createDefinitionParams(position: Position): TextDocumentPositionParams{
        return {
            textDocument: { uri: this.document.uri.path },
            position: position
        };
    }
    //locates the text in the range
    private locateText(text: string, range1: Range): Range | undefined{
        let range = this.vscodeRange(range1);
        let text_in_range = this.document.getText(range);

        let index = text_in_range.indexOf(text);

        if(index == -1){
            return undefined;
        }

        let start_offset = this.document.offsetAt(range.start) + index;
        let start = this.document.positionAt(start_offset);

        let end = Position.create(start.line, start.character + text.length);

        return Range.create (start, end);
    }

    private getLine(range: Range): Range {
        let line = range.start.line; // Get the line number from the range
    
        // Create a range that covers the entire line
        let line_start = Position.create(line, 0);
        let line_text = this.document.getText( new vscode.Range(this.vscodePosition(line_start), new vscode.Position(line + 1, 0)));
        let line_end = Position.create(line, line_text.length);
    
        let line_range =  Range.create(line_start, line_end);

        return line_range;
    }

    private getSearchRange(): Range {
        this.output_manager.log(JPipeOutput.DEBUG, "Getting search range");
        const start = Position.create(0, 0);
        const lastLine = this.document.lineCount - 1;

        const last_line_length = this.document.getText( this.vscodeRange(Range.create(Position.create(lastLine, 0), Position.create(lastLine + 1, 0)))).length;
        this.output_manager.log(JPipeOutput.DEBUG, "line length gotten"); 

        const end = Position.create(lastLine, last_line_length-1);

        return Range.create(start, end);
    }

    private vscodeRange(range: Range): vscode.Range{
        return new vscode.Range(this.vscodePosition(range.start), this.vscodePosition(range.end));
    }

    private vscodePosition(position: Position): vscode.Position{
        return new vscode.Position(position.line, position.character);
    }
}