import * as vscode from 'vscode';
import { AbstractConfiguration, ConfigKey, Configuration } from "./abstract-configuration.js"

//class to keep track of checkGraphviz setting
export class CheckGraphviz implements AbstractConfiguration<boolean>{
    public readonly configuration: Configuration<boolean> = {
        key: ConfigKey.CHECKGRAPHVIZ,
        default_value: false
    };
    
    private value: boolean;

    public constructor(){
        this.value = this.update();
    }

    //updates value of configuration
    public update(): boolean{
        let check_graphviz: boolean;
        let configuration = vscode.workspace.getConfiguration().inspect(this.configuration.key)?.globalValue;

        if(typeof configuration === "boolean"){
            check_graphviz = true;
        }else{
            check_graphviz = false;
        }

        this.value = check_graphviz;

        return check_graphviz;
    }

    //returns current configuration
    public getConfiguration(): boolean {
        return this.value;
    }
}