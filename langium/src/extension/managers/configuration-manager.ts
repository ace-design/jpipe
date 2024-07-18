import * as vscode from 'vscode';
import { EventSubscriber, isConfigurationChangeEvent, isTextEditor } from './event-manager.js';

type Configuration<T> = {
    readonly key: ConfigKey,
    readonly update_function: () => T,
    value: T
}

//keeps track of values of configuration settings
export class ConfigurationManager implements EventSubscriber<vscode.TextEditor | undefined>, EventSubscriber<vscode.ConfigurationChangeEvent>{
    // Output channel used for debugging
    private output_channel: vscode.OutputChannel;

    //list of all configurations including their key, update function, and current associated value
    private configurations: Configuration<any>[];

    //map to reference from key, the location of the configuration values in configurations array
    private configuration_indices: Map<string, number>;

    //current directory
    private directory!: vscode.WorkspaceFolder;
    
    constructor(private readonly context: vscode.ExtensionContext, output_channel: vscode.OutputChannel){
        this.output_channel = output_channel;

        this.update(vscode.window.activeTextEditor);

        this.configurations = [
            {
                key: ConfigKey.LOGLEVEL,
                update_function: this.updateLogLevel,
                value: this.updateLogLevel()
            },
            {
                key: ConfigKey.JARFILE,
                update_function: this.updateJarFile,
                value: this.updateJarFile()
            },
            {
                key: ConfigKey.DEVMODE,
                update_function: this.updateDeveloperMode,
                value: this.updateDeveloperMode()
            }
        ]

        this.configuration_indices = this.setIndices(this.configurations);   
    }


    //updater functions
	public async update(change: vscode.ConfigurationChangeEvent): Promise<void>;
    public async update(editor: vscode.TextEditor | undefined): Promise<void>;
	public async update(data: vscode.ConfigurationChangeEvent | vscode.TextEditor | undefined): Promise<void>{
        if(isTextEditor(data)){
            this.updateEditor(data);
        }else if(isConfigurationChangeEvent(data)){
            this.updateConfiguration(data);
		}
    }

    //helper function to manage configuration updates
    private updateConfiguration(configuration_change: vscode.ConfigurationChangeEvent): void{
        this.configurations.forEach((configuration)=>{
            if(configuration_change.affectsConfiguration(configuration.key)){
                try{
                    configuration.value = configuration.update_function.call(this);
                }catch(error: any){
                    this.output_channel.appendLine(error);
                }
                
            }
        });
    }
    
    //helper function to manage editor updates
    private updateEditor(editor: vscode.TextEditor | undefined): void{
        if(editor){
            let document = editor.document
            let directory = vscode.workspace.getWorkspaceFolder(document.uri);

            if(directory){
                this.directory = directory;
            }
        }
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
        let default_path: string;

		let default_value = "";//must be kept in sync with the actual default value manually
		let configuration = vscode.workspace.getConfiguration().inspect(ConfigKey.JARFILE)?.globalValue;
		
		if(typeof configuration === "string"){
			jar_file = configuration;
		}else{
			jar_file = default_value;
		}
		
        default_path = vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').path;
		
        if(jar_file === default_value){
			jar_file = default_path;
			vscode.workspace.getConfiguration().update(ConfigKey.JARFILE, jar_file);
		}else if(!this.jarPathExists(jar_file)){
            let developer_mode = this.getConfiguration(ConfigKey.DEVMODE);
            if(developer_mode){
                throw new Error("This file does not exist, please try again");
            }else{
                jar_file = vscode.Uri.joinPath(this.context.extensionUri, 'jar', 'jpipe.jar').path;
            }
            
		}

		return jar_file;
	}


    //getter function to return the current value of any configuration being monitored
    public getConfiguration(configuration_key: ConfigKey): any{
        let target_config: any;
        
        let config_index = this.configuration_indices.get(configuration_key);

        if(config_index !== undefined){    
            target_config = this.configurations[config_index].value;
        }else{
            throw new Error("Configuration: " + configuration_key + " cannot be found in configuration key list");
        }

        return target_config; 
    }

    //helper function to verify jar file path
	private jarPathExists(file_path: string): boolean{
		let jar_path_exists: boolean;
		
		try{
			let file = vscode.Uri.file(file_path);
			let new_uri =  vscode.Uri.joinPath(this.directory.uri, "example.jar");

			vscode.workspace.fs.copy(file, new_uri);
			vscode.workspace.fs.delete(new_uri);
			
			jar_path_exists = true;
		}catch(error: any){
			this.output_channel.appendLine(error.toString());

			jar_path_exists = false;
		}
		return jar_path_exists;
	}

    //helper function to parse through the configuration array, and set a search location based on configuration key
    private setIndices(array: Configuration<any>[]): Map<string, number>{
        let map: Map<string, number> = new Map();
        
        let counter = 0;
        
        array.forEach(configuration =>{
            map.set(configuration.key, counter);

            counter++;
        })

        return map;
    }
}

export enum ConfigKey{
    LOGLEVEL = "jpipe.logLevel",
    JARFILE = "jpipe.jarFile",
    DEVMODE = "jpipe.developerMode"
}