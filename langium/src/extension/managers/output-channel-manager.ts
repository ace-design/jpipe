import * as vscode from "vscode"

//All output channels that can be accessed by output manager
export enum JPipeOutput{
    USER = "jpipe_user",
    CONSOLE = "jpipe_console",
    IMAGE = "jpipe_image"
}

//helper class so that all objects needing logs can access all log channels, automatically generates new output channels when they are added to the enum
export class OutputManager{
    private channels: Map<JPipeOutput, vscode.OutputChannel>;

    constructor(){
        this.channels = new Map<JPipeOutput, vscode.OutputChannel>();
        
        Object.values(JPipeOutput).forEach(key =>{
            this.addChannel(key);
        })
     
    }
    //helper function to add a channel to the output manager
    private addChannel(channel: JPipeOutput): void{
        this.channels.set(channel, vscode.window.createOutputChannel(channel));
    }

    //function to add logs to output channels, can be altered for superior formatting in future
    public log(channel: JPipeOutput, info: string): void{
        let output = this.channels.get(channel);

        if(output){
            output.appendLine(info);
        }
    }
}

