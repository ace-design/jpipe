import * as vscode from 'vscode';
import { AbstractConfiguration, ConfigKey } from "./abstract-configuration.js"

//class to monitor developerMode setting
export class DeveloperMode implements AbstractConfiguration<boolean>{
    public readonly key = ConfigKey.DEVMODE;
    public readonly default_value = false;
    private value: boolean;

    public constructor(){
        this.value = this.update();
    }

    //updates developerMode setting
    public update(): boolean{
        let developer_mode: boolean;
        let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.DEVMODE)?.globalValue;

        if(typeof configuration === "boolean"){
            developer_mode = true;
        }else{
            developer_mode = false;
        }

        this.value = developer_mode;
        return developer_mode;
    }

    //returns current configuration
    public getConfiguration(): boolean {
        return this.value;
    }
}