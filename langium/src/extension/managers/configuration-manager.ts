import * as vscode from 'vscode';
import { EventSubscriber, isConfigurationChangeEvent } from './event-manager.js';
import { } from '../configuration/abstract-configuration.js';
import { LogLevel, AbstractConfiguration, ConfigKey, DeveloperMode, CheckGraphviz, CheckJava, JarFile } from '../configuration/index.js';

const fs = require("fs");


//keeps track of values of configuration settings
export class ConfigurationManager implements EventSubscriber<vscode.ConfigurationChangeEvent>{
    
    // Output channel used for debugging
    private output_channel: vscode.OutputChannel;

    //list of all configurations including their key, update function, and current associated value
    private configurations: Map<ConfigKey, AbstractConfiguration<any>>;
    
    constructor(context: vscode.ExtensionContext, output_channel: vscode.OutputChannel){
        this.output_channel = output_channel;

        this.configurations = new Map<ConfigKey, AbstractConfiguration<any>>;
        
        const configurations_list: AbstractConfiguration<any>[] = [
            new LogLevel(),
            new DeveloperMode(),
            new CheckGraphviz(),
            new CheckJava(),
            new JarFile(context, fs)
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

        return configuration.getConfiguration(); 
    }

    //updates on configuration change events
	public async update(change: vscode.ConfigurationChangeEvent): Promise<void>{
        this.configurations.forEach((configuration)=>{
            if(change.affectsConfiguration(configuration.key)){
                this.tryUpdate(configuration);
            }
        });
    }

    //helper function to attempt update and check dev mode or set to default
    private tryUpdate(configuration: AbstractConfiguration<any>): void{
        try{
            configuration.update();
        }catch(error: any){
            if(this.getConfiguration(ConfigKey.DEVMODE)){
                this.output_channel.appendLine("Configuration: " + error);
            }else{
                this.output_channel.appendLine("Configuration: Error found, using default value!");
            }
        }
    }
}