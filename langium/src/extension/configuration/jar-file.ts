import * as vscode from 'vscode';
import { AbstractConfiguration, ConfigKey } from "./abstract-configuration.js"

//class to monitor jarFile setting
export class JarFile implements AbstractConfiguration<string>{
    public readonly key = ConfigKey.JARFILE;
    public readonly default_value = this.getDefaultJar();
    private value: string;

    public constructor(private readonly context: vscode.ExtensionContext, private readonly fs: any, private readonly output_channel: vscode.OutputChannel){
        try{
            this.value = this.update();
        }catch(error){
            this.output_channel.appendLine("Using default jar file")
            this.value = this.default_value;
        }

    }

    //updates jarFile setting
    public update(): string{
        let jar_file: string;
        
        let default_value = "";//must be kept in sync with the actual default value manually
        let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.JARFILE)?.globalValue;
        
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
        return vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').path;
    }

    //helper function to verify jar file path
    private jarPathExists(file_path: string): boolean{
        return this.fs.existsSync(file_path);
    }
}