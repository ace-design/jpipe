import * as vscode from 'vscode';

import { AbstractConfiguration, ConfigKey, Configuration } from "./abstract-configuration.js"
import { JPipeOutput, OutputManager, JpipeFileSystemManager } from '../managers/index.js';

//class to monitor jarFile setting
export class JarFile implements AbstractConfiguration<string>{
    public readonly configuration: Configuration<string> = {
        key: ConfigKey.JARFILE,
        default_value: this.getDefaultJar()
    };

    private value: string;

    public constructor(private readonly context: vscode.ExtensionContext, private readonly fs: JpipeFileSystemManager, private readonly output_channel: OutputManager){
        try{
            this.value = this.update();
        }catch(error){
            this.output_channel.log(JPipeOutput.USER, "Using default jar file")
            this.value = this.configuration.default_value;
        }

    }

    //updates jarFile setting
    public update(): string{
        let jar_file: string;
        
        let default_value = "";//must be kept in sync with the actual default value manually
        let configuration = vscode.workspace.getConfiguration().inspect(this.configuration.key)?.globalValue;
        
        jar_file = typeof configuration === "string" ? configuration : default_value;
        
        if(jar_file === default_value){//on startup, sets jar_file to default value
            jar_file = this.getDefaultJar();
            vscode.workspace.getConfiguration().update(ConfigKey.JARFILE, jar_file);
        }else if(!this.jarPathExists(jar_file)){
            throw new Error("This file does not exist");
        }

        return jar_file;
    }

    //returns current jarFile setting
    public getConfiguration(): string {
        return this.value;
    }

    //helper function to get the default jar path
    private getDefaultJar(): string{
        return vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').fsPath;
    }

    //helper function to verify jar file path
    private jarPathExists(file_path: string): boolean{
        return this.fs.pathExists(file_path);
    }
}