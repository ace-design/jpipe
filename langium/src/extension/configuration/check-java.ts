import * as vscode from 'vscode';
import { AbstractConfiguration, ConfigKey } from "./abstract-configuration.js"

//class to monitor checkJava setting
export class CheckJava implements AbstractConfiguration<boolean>{
    public readonly key = ConfigKey.CHECKJAVA;
    public readonly default_value = false;
    private value: boolean;

    public constructor(){
        this.value = this.update();
    }

    //updates checkJava setting
    public update(): boolean{
        let check_java: boolean;
        let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.CHECKJAVA)?.globalValue;

        if(typeof configuration === "boolean"){
            check_java = true;
        }else{
            check_java = false;
        }

        this.value = check_java;

        return check_java;
    }

    //returns current checkJava setting
    public getConfiguration(): boolean {
        return this.value;
    }
}