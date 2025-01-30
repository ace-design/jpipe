import * as vscode from 'vscode';

import { JpipeFileSystemManager, JPipeOutput, OutputManager } from "../managers/index.js";
import { AbstractConfiguration, Configuration } from "./index.js";

//generic class to create a configuration of a path with a set default value
export class PathConfiguration implements AbstractConfiguration<string>{
    private value: string;

    public constructor(private readonly fs: JpipeFileSystemManager, private readonly output_channel: OutputManager, public readonly configuration: Configuration<string>){
        try{
            this.value = this.update();
        }catch(error){
            this.output_channel.log(JPipeOutput.USER, "Using default file path for setting");
            
            this.value = this.getDefaultValue();
        }
    }

    //updates configuration
    public update(): string {
        let path: string;

        let configuration = vscode.workspace.getConfiguration().inspect(this.configuration.key)?.globalValue;
        path = typeof configuration === "string" ? configuration : this.configuration.default_value;

        if(path === undefined || !(this.fs.pathExists(path) || path === this.configuration.default_value)){
            path = this.getDefaultValue();
            vscode.workspace.getConfiguration().update(this.configuration.key, path);

            throw new Error("This file does not exist, using default value: " + path);
        }


        return path;
    }

    //returns configuration value
    public getConfiguration(): string {
        return this.value;
    }

    //helper function to get the default value
    protected getDefaultValue(): string{
        return this.configuration.default_value;
    };
}