import * as vscode from 'vscode';
import { EventSubscriber, isConfigurationChangeEvent, isTextEditor } from './event-manager.js';
const fs = require("fs");

type Configuration<T> = {
    readonly key: ConfigKey,
    readonly update_function: () => T,
    value: T
    default_value: () => T
}

//keeps track of values of configuration settings
export class ConfigurationManager implements EventSubscriber<vscode.ConfigurationChangeEvent>{
    
    // Output channel used for debugging
    private output_channel: vscode.OutputChannel;

    //list of all configurations including their key, update function, and current associated value
    private configurations: Map<ConfigKey, Configuration<any>>;
    
    constructor(private readonly context: vscode.ExtensionContext, output_channel: vscode.OutputChannel){
        this.output_channel = output_channel;

        this.update(vscode.window.activeTextEditor);
        this.configurations = new Map<ConfigKey, Configuration<any>>;
        
        const configurations_list: Configuration<any>[] = [
            {
                key: ConfigKey.LOGLEVEL,
                update_function: () => this.updateLogLevel(),
                value: this.updateLogLevel(),
                default_value: () => "error"
            },
            {
                key: ConfigKey.JARFILE,
                update_function: () => this.updateJarFile(),
                value: this.updateJarFile(),
                default_value: () => this.getDefaultJar()
            },
            {
                key: ConfigKey.DEVMODE,
                update_function: () => this.updateDeveloperMode(),
                value: this.updateDeveloperMode(),
                default_value: () => false
            }
        ]

        configurations_list.forEach((config =>{
            this.configurations.set(config.key,config);
        }))  
    }

    //getter function to return the current value of any configuration being monitored
    public getConfiguration(configuration_key: ConfigKey): any{
        let configuration = this.configurations.get(configuration_key)

        if(configuration === undefined){    
            throw new Error("Configuration: " + configuration_key + " cannot be found in configuration key list");
        }

        return configuration.value; 
    }

    //updater functions
	public async update(change: vscode.ConfigurationChangeEvent): Promise<void>;
    public async update(editor: vscode.TextEditor | undefined): Promise<void>;
	public async update(data: vscode.ConfigurationChangeEvent | vscode.TextEditor | undefined): Promise<void>{
        if(isConfigurationChangeEvent(data)){
            this.updateConfiguration(data);
		}
    }

    //helper function to manage configuration updates
    private updateConfiguration(configuration_change: vscode.ConfigurationChangeEvent): void{
        this.configurations.forEach((configuration)=>{
            if(configuration_change.affectsConfiguration(configuration.key)){
                try{
                    configuration.value = configuration.update_function();
                }catch(error: any){
                    if(this.getConfiguration(ConfigKey.DEVMODE)){
                        configuration.value = undefined;
                        this.output_channel.appendLine("Configuration: " + error);
                    }else{
                        configuration.value = configuration.default_value();
                        this.output_channel.appendLine("Configuration: Error found, using default value!");
                    }

                }
                
            }
        });
    }

    //helper function to fetch the current log level
    private updateLogLevel(): string{
        let log_level: string;
		let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.LOGLEVEL)?.globalValue;
			
		if(typeof configuration === "string"){
			log_level = configuration;
		}else{
			log_level = "error";
		}

		return log_level;
    }

    //helper function to fetch the current developer mode setting
    private updateDeveloperMode(): boolean {
        let developer_mode: boolean;
		let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.DEVMODE)?.globalValue;

        if(typeof configuration === "boolean"){
            developer_mode = configuration;
        }else{
            developer_mode = false;
        }

        return developer_mode;
    }

    //helper function to fetch and verify the input jar file path
	private updateJarFile(): string{
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

    //helper function to get the default jar path
    private getDefaultJar(): string{
        return vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').path;
    }

    //helper function to verify jar file path
	private jarPathExists(file_path: string): boolean{
		let jar_path_exists: boolean;
        let uri = vscode.Uri.file(file_path);

        try {
            vscode.workspace.fs.stat(uri);
            jar_path_exists = true;
        } catch {
            vscode.window.showInformationMessage(`${uri.toString(true)} file does *not* exist`);
            jar_path_exists = false;
        }

		return fs.existsSync(file_path);
	}
}

export enum ConfigKey{
    LOGLEVEL = "jpipe.logLevel",
    JARFILE = "jpipe.jarFile",
    DEVMODE = "jpipe.developerMode"
}