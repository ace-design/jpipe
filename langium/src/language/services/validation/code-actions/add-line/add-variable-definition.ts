import { AbstractAddLine } from "./abstract-add-line.js";

//Class that adds a conclusion into the first line of the diagram selected
export class AddVariableDefinitionLine extends AbstractAddLine{
    public override title: string;
    private readonly variable: VariableType;
    
    private readonly variable_map: Map<VariableType, string>;
    
    public constructor(code: string, variable: VariableType){
        super(code);

        this.title = "Add " + variable;
        this.variable = variable;

        this.variable_map = new Map<VariableType, string>();
        this.fillVariableMap(this.variable_map);
    }

    //helper function to fill the variable map
    private fillVariableMap(variable_map: Map<VariableType, string>): void{
        variable_map.set(VariableType.CONCLUSION, "c");
        variable_map.set(VariableType.SUPPORT, "s");
        variable_map.set(VariableType.EVIDENCE, "e");
        variable_map.set(VariableType.STRATEGY, "s");
        variable_map.set(VariableType.SUBCONCLUSION, "sc");
    }

    //helper function to return new line text based on VariableType
    protected override getNewLineText(): string{
        let variable_name: string;
        let new_line_text: string;

        let variable_name_fetch = this.variable_map.get(this.variable);
        variable_name = variable_name_fetch ? variable_name_fetch : "variable_name"

        new_line_text = this.variable + " " + variable_name + " is \"\"";

        return new_line_text;
    };
}

export enum VariableType{
    EVIDENCE = "evidence",
    STRATEGY = "strategy",
    SUBCONCLUSION = "sub-conclusion",
    CONCLUSION = "conclusion",
    SUPPORT = "@support"
}