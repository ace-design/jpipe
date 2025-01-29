import * as vscode from 'vscode';
import { EventSubscriber } from './event-manager.js';
import { LogLevel, AbstractConfiguration, ConfigKey, DeveloperMode, CheckGraphviz, CheckJava, JarFile } from '../configuration/index.js';
import { JPipeOutput, OutputManager } from './index.js';
import { JpipeFileSystemManager } from './file-system-manager.js';

//keeps track of values of configuration settings
export class ConfigurationManager implements EventSubscriber<vscode.ConfigurationChangeEvent>{

    //list of all configurations including their key, update function, and cgiturrent associated value
    private configurations: Map<ConfigKey, AbstractConfiguration<any>>;
    
    constructor(context: vscode.ExtensionContext, private readonly output_manager: OutputManager, fs: JpipeFileSystemManager){

        this.configurations = new Map<ConfigKey, AbstractConfiguration<any>>;
        
        const configurations_list: AbstractConfiguration<any>[] = [
            new LogLevel(),
            new DeveloperMode(),
            new CheckGraphviz(),
            new CheckJava(),
            new JarFile(context, fs, output_manager)
        ]

        configurations_list.forEach((config =>{
            this.configurations.set(config.configuration.key,config);
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
            if(change.affectsConfiguration(configuration.configuration.key)){
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
                this.output_manager.log(JPipeOutput.CONSOLE, "Configuration: " + error); //adds to console because in devmode
            }else{
                this.output_manager.log(JPipeOutput.USER, "Configuration: Error found, using default value!");
            }
        }
    }
}