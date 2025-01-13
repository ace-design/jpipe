import { AbstractEnvironmentCheck, EnvironmentCommand, GraphvizEnvironmentCheck, JavaEnvironmentCheck } from "../environment-tests/index.js";
import * as vscode from 'vscode';
import { ConfigKey, ConfigurationManager } from "./configuration-manager.js";

type EnvironmentCheck = {
    env_check: AbstractEnvironmentCheck
    config_key?: ConfigKey
}

//Manager class to run all environment checks on startup
export class EnvironmentCheckManager{
    //list of all environment checks to run on startup
    private environment_checkers: Set<EnvironmentCheck>;

    //manages configurations
    private configuration: ConfigurationManager; 

    constructor(configuration: ConfigurationManager){
        const environment_checkers_list: Array<EnvironmentCheck> = [
            {
                env_check: new JavaEnvironmentCheck(),
                config_key: ConfigKey.CHECKJAVA
            },
            {
                env_check: new GraphvizEnvironmentCheck(),
                config_key: ConfigKey.CHECKGRAPHVIZ
            }
        ]

        this.environment_checkers = new Set<EnvironmentCheck>();

        environment_checkers_list.forEach((check) => {
            this.environment_checkers.add(check);
        })

        this.configuration = configuration;
    }

    //main function to run all environment checks
    public run(): void{
        this.environment_checkers.forEach(check =>{
                let enabled = check.config_key ? this.configuration.getConfiguration(check.config_key) : true; //checks the configuration if it exists (BOOLEANS ONLY), otherwise just runs checks

                if(enabled){
                    this.runCheck(check.env_check);
                }
            }
        )
    }

    //helper function to run a singular environment check
    private async runCheck(check: AbstractEnvironmentCheck): Promise<void>{
        let result = await check.checkEnvironment();
        
        if(result){    
            let VSCodeCommand = this.getVSCodeCommand(result.command);
            
            VSCodeCommand(result.message);
        }
    }

    //helper function to translate environment command enum values to vscode window commands
    private getVSCodeCommand(command: EnvironmentCommand): (arg: string) => Thenable<string | undefined>{
        let VSCodeCommand!: (arg: string) => Thenable<string | undefined>;//warning

        if(command == EnvironmentCommand.ERROR_MESSAGE){
            VSCodeCommand = vscode.window.showErrorMessage
        }else if (command == EnvironmentCommand.INFORMATION_MESSAGE){
            VSCodeCommand = vscode.window.showInformationMessage
        }

        return VSCodeCommand;
    };
}