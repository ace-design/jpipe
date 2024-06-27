import * as vscode from 'vscode';

//monitors and updates any context keys
export class ContextMonitor{
    private contexts: Context[];
    private document!: vscode.TextDocument;
    private selection!: vscode.Selection;

    constructor(editor: vscode.TextEditor | undefined){
        this.contexts = [
            {
                context_key: 'jpipe.atJustification',
                function: this.cursorAt,
                params: 'justification'
            }
        ];

        this.updateEditor(editor);
    }

    //updates the current editor
    public async updateEditor(editor: vscode.TextEditor | undefined){
        if(!editor){
            return;
        }
        this.document = editor.document;
    }

    //updates the context keys when the selection changes
    public async updateTextSelection(selections: readonly vscode.Selection[]){
        if(selections.length === 1){
            this.selection = selections[0];
        }

        this.contexts.forEach((context)=>{
            vscode.commands.executeCommand('setContext',context.context_key, context.function.call(this, context.params));  
        });
    }
    
    //helper function which checks if the cursor is at a certain class type
    private cursorAt(class_type: string): boolean{
        let class_correct = false;
        let diagram_starts = false;
        let diagram_ends = false;

        let word_range = this.document.getWordRangeAtPosition(this.selection.active);
            
        if(word_range){
            class_correct = this.isClassCorrect(word_range, this.document, class_type);
            diagram_starts = this.diagramStarts(word_range, this.document);
            diagram_ends = this.diagramEnds(word_range, this.document);
        }

        return class_correct && diagram_starts && diagram_ends;
    }


    //helper function to determine if the class type of a selected word is correct
    private isClassCorrect(word_range: vscode.Range, document: vscode.TextDocument, class_type: string): boolean{
        let class_name = document.getText(word_range);
        let class_name_correct = class_name !== class_type;

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
        let range = this.getRange(word_range.end, document);
        let range_text = document.getText(range);

        while(range_text && !range_text.includes('}')){
            range = this.getRange(range.end, document);
            range_text = document.getText(range);
        }

        return range_text !== undefined;
    }

    //helper function to make diagramEnds more clear
    private getRange(start_pos: vscode.Position, document: vscode.TextDocument): vscode.Range{
        let offset_end = document.offsetAt(start_pos)+1;
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