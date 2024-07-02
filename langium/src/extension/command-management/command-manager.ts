import * as vscode from 'vscode';

export class CommandManager{

    constructor(private readonly context:vscode.ExtensionContext){
    }

    public register(command_users: CommandUser[]): void;
    public register(command_users: CommandUser): void;
    public register(command_users: CommandUser | CommandUser[]): void{
        if(Array.isArray(command_users)){
            command_users.forEach((command_user) => {
                this.registerCommands(command_user);
            });
        }else{
            this.registerCommands(command_users);
        }

    }

    private registerCommands(command_user: CommandUser){
        let command_list = command_user.getCommands();

        if(Array.isArray(command_list)){
            command_list.forEach((command) =>{
                this.context.subscriptions.push(vscode.commands.registerCommand(command.command, command.callback, command.thisArg));
            });
            
        }else{
            this.context.subscriptions.push(vscode.commands.registerCommand(command_list.command, command_list.callback, command_list.thisArg));
        }
    }
}

export interface CommandUser{
    getCommands(): Command[] | Command;
}

export type Command = {
    command: string,
    callback: (...args: any[]) => any,
    thisArg?: any
}