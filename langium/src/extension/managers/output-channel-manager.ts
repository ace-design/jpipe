import * as vscode from "vscode"

//All output channels that can be accessed by output manager
export enum JPipeOutput{
    USER = "jpipe_user",
    CONSOLE = "jpipe_console",
    IMAGE = "jpipe_image",
    DEBUG = "jpipe_debug"
}

//helper class so that all objects needing logs can access all log channels, automatically generates new output channels when they are added to the enum
export class OutputManager{
    private channels: Map<JPipeOutput, vscode.OutputChannel>;

    private inactive_channels: Array<JPipeOutput>;
    
    constructor(){
        this.channels = new Map<JPipeOutput, vscode.OutputChannel>();

        this.inactive_channels = [
            //JPipeOutput.DEBUG
        ]

        Object.values(JPipeOutput).forEach(key =>{
            if(this.active(key)){
                this.addChannel(key)
            }
        })
    }

    //helper function to add a channel to the output manager
    private addChannel(channel: JPipeOutput): void{
        this.channels.set(channel, vscode.window.createOutputChannel(channel));
    }

    //function to add logs to output channels, can be altered for superior formatting in future
    public log(channel: JPipeOutput, info: string): void{
        if(this.active(channel)){
            let output = this.channels.get(channel);

            if(output){
                output.appendLine(info);
            }
        }
    }

    //helper function to determine an output channel's activity status
    private active(channel: JPipeOutput): boolean{
        return !this.inactive_channels.includes(channel);
    }
}

