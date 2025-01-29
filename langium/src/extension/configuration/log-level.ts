import * as vscode from 'vscode';
import { AbstractConfiguration, ConfigKey, Configuration } from "./abstract-configuration.js"

//class to monitor logLevel setting
export class LogLevel implements AbstractConfiguration<string>{
    public readonly configuration: Configuration<string> = {
        key: ConfigKey.LOGLEVEL,
        default_value: "error"
    };

    private value: string;

    public constructor(){
        this.value = this.update();
    }

    //updates logLevel setting
    public update(): string{
        let log_level: string;
        let configuration = vscode.workspace.getConfiguration().inspect(this.configuration.key)?.globalValue;
            
        if(typeof configuration === "string"){
            log_level = configuration;
        }else{
            log_level = "error";
        }

        this.value = log_level;

        return log_level;
    }

    //returns current configuration
    public getConfiguration(): string {
        return this.value;
    }
}