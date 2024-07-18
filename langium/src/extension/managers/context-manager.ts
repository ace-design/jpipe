import * as vscode from 'vscode';
import { EventSubscriber, isTextEditor, isTextEditorSelectionChangeEvent } from './event-manager.js';

//monitors and updates any context keys
export class ContextManager implements EventSubscriber<vscode.TextEditor | undefined>, EventSubscriber<vscode.TextEditorSelectionChangeEvent>{
    //keeps track of which contexts need to be monitored
    private contexts: Context[];

    //current text document
    private document!: vscode.TextDocument | undefined;

    //current text selection
    private selection!: vscode.Selection;

    constructor(editor: vscode.TextEditor | undefined){
        this.contexts = [
            {
                context_key: 'jpipe.atJustification',
                function: this.cursorAt,
                params: 'justification'
            },
            {
                context_key: 'jpipe.inJustification',
                function: this.cursorIn,
                params: 'justification'
            }
        ];

        this.update(editor);
    }

    //updates the current text document/text selection
    public async update(editor: vscode.TextEditor | undefined): Promise<void>;
    public async update(changes: vscode.TextEditorSelectionChangeEvent): Promise<void>;
    public async update(data: (vscode.TextEditor | undefined) | vscode.TextEditorSelectionChangeEvent): Promise<void>{
        if(isTextEditorSelectionChangeEvent(data)){
            if(data.selections.length === 1){
                this.selection = data.selections[0];
            }

            this.contexts.forEach((context)=>{
                vscode.commands.executeCommand('setContext',context.context_key, context.function.call(this, context.params));  
            });
        }
        else if(isTextEditor(data)){
            if(data !== undefined && data.document.languageId == "jpipe"){
                this.document = data.document;
            }else{
                this.document = undefined;
            }

            this.contexts.forEach((context)=>{
                vscode.commands.executeCommand('setContext',context.context_key, context.function.call(this, context.params));  
            });
        }
    }

    //checks if the cursor is in a diagram (between '{' and '}')
    private cursorIn(class_type: string): boolean{
        let cursor_at: boolean = false;
        
        try{
            if(this.document && this.selection){
                let word_range = this.findDiagramNameRange(this.selection.active, this.document);
                
                cursor_at = this.cursorAt(class_type, word_range);
            }
        }catch(error: any){

        }

        return cursor_at;
    }

    //helper function which checks if the cursor is at a certain class type
    private cursorAt(class_type: string, word_range: vscode.Range): boolean;
    private cursorAt(class_type: string): boolean;
    private cursorAt(class_type: string, word_range?: vscode.Range): boolean{
        let class_correct = false;
        let diagram_starts = false;
        let diagram_ends = false;
        
        if(this.document){
            if(word_range === undefined && this.selection){
                word_range = this.document.getWordRangeAtPosition(this.selection.active);
            }
                
            if(word_range){
                class_correct = this.isClassCorrect(word_range, this.document, class_type);
                diagram_starts = this.diagramStarts(word_range, this.document);
                diagram_ends = this.diagramEnds(word_range, this.document);
            }
        }

        return class_correct && diagram_starts && diagram_ends;
    }

    //helper function to determine if the class type of a selected word is correct
    private isClassCorrect(word_range: vscode.Range, document: vscode.TextDocument, class_type: string): boolean{
        //determine that the cursor is selected on the diagram title
        let class_name = document.getText(word_range);
        let class_name_correct = class_name !== class_type;

        //determine that the class type (ex. justification) is correct
        let class_range = document.getWordRangeAtPosition(word_range.start.translate(0, -1));
        let class_type_correct = document.getText(class_range) === class_type;
        
        return (class_name_correct && class_type_correct);
    }

    //helper function for the class type verification process
    private diagramStarts(word_range: vscode.Range, document: vscode.TextDocument): boolean{
        let new_end = word_range.end.translate(0,2); 
        let search_range = new vscode.Range(word_range.end, new_end);
        let search_text = document.getText(search_range);
        
        return search_text.includes('{');
    }

    //helper function to determine if the diagram ends
    private diagramEnds(word_range: vscode.Range, document: vscode.TextDocument): boolean{
        let range: vscode.Range;
        let range_text: string | undefined;
       
        try{
            range = this.findRange('}', Direction.FORWARD, word_range, document);
            range_text = document.getText(range);
        }catch(error: any){
            range_text === undefined;
        }

        return range_text !== undefined;
    }

    //helper function to find the range where the diagram name is found
    private findDiagramNameRange(position: vscode.Position, document: vscode.TextDocument): vscode.Range{
        let pos: vscode.Position;
        let word_range: vscode.Range | undefined;
        let range: vscode.Range;
        
        try{
            let end_position = this.findRange('{', Direction.BACKWARD, this.getRange(position, document, -1), document).start;
            range = this.getRange(end_position, document, -1);

            if(!range){
                throw new Error("Text document does not include diagram name or class title");
            }

            pos = range.start;

            if(document.getText(range) === ' '){
                pos = pos.translate(0,-1);
            }
    
            word_range = document.getWordRangeAtPosition(pos);
        }catch(error: any){
            
        }

        if(!word_range){
            throw new Error("Range of diagram name cannot be found");
        }

        return word_range;
    }

    //helper function to find the range of a string looking in a specific direction, from a start range, in a document
    private findRange(key: string, direction: Direction, start_range: vscode.Range, document: vscode.TextDocument): vscode.Range{
        let range: vscode.Range;
        let range_text: string;
        let range_function = (): vscode.Range =>{
            return new vscode.Range(new vscode.Position(0,0), new vscode.Position(0,0));
        }

        if(direction === Direction.FORWARD){
            range = this.getRange(start_range.end, document);
            range_text = document.getText(range);
            
            range_function = (): vscode.Range =>{
                return this.getRange(range.end, document, 1);
            };
        }else{
            range = this.getRange(start_range.start, document, -1);
            range_text = document.getText(range);
            
            range_function = (): vscode.Range =>{
                return this.getRange(range.start, document, -1);
            };
        }

        while(range_text && !range_text.includes(key)){
            range = range_function();
            range_text = document.getText(range);
        }
        
        if(!range_text || !range){
            throw new Error("Specified range cannot be found");
        }

        return range;
    }

    //helper function to make diagramEnds more clear
    private getRange(start_pos: vscode.Position, document: vscode.TextDocument): vscode.Range
    private getRange(start_pos: vscode.Position, document: vscode.TextDocument, offset: number): vscode.Range;
    private getRange(start_pos: vscode.Position, document: vscode.TextDocument, offset?: number): vscode.Range{
        if(offset === undefined){
            offset = 1;
        }
        
        let offset_end = document.offsetAt(start_pos)+offset;
        let end_pos = document.positionAt(offset_end);

        return new vscode.Range(start_pos, end_pos)
    }
}

//stores necessary information for setting the context key
type Context = {
    context_key: string,
    function: Function,
    params: string
}

enum Direction{
    FORWARD,
    BACKWARD
}