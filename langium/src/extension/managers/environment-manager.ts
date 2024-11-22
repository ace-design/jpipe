import { AbstractEnvironmentCheck, EnvironmentCommand, GraphvizEnvironmentCheck, JavaEnvironmentCheck } from "../environment-tests/index.js";
import * as vscode from 'vscode';

//Manager class to run all environment checks on startup
export class EnvironmentCheckManager{
    private environment_checkers: Set<AbstractEnvironmentCheck>;

    constructor(){
        this.environment_checkers = new Set<AbstractEnvironmentCheck>([
            new JavaEnvironmentCheck(),
            new GraphvizEnvironmentCheck()
        ])
    }

    //main function to run all environment checks
    public run(): void{
        this.environment_checkers.forEach(check =>
            this.runCheck(check)
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