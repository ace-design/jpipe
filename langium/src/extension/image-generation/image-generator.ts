import * as vscode from 'vscode';
import util from "node:util";
import { SaveImageCommand } from './save-image-command.js';
import { Command, CommandUser } from '../command-management/command-manager.js';

export class ImageGenerator implements CommandUser{
	// Defines the command needed to execute the extension. 
	private save_image_command: SaveImageCommand;

	private types: ImageType[];
	
	// New channel created in vscode terminal for user debugging.
	private static output_channel = vscode.window.createOutputChannel("jpipe_image");
    
	constructor(private readonly context: vscode.ExtensionContext, save_image_command: SaveImageCommand) {
		this.save_image_command = save_image_command;
		this.types = [
			{
				exe_command: "jpipe.downloadPNG", 
				format: Format.PNG
			},
			{
				exe_command: "jpipe.downloadSVG", 
				format: Format.SVG
			}
		];
	}

	public getCommands(): Command[] | Command{
		let command_list: Command[] = [];
		this.types.forEach( (type) =>{
			command_list.push({
				command: type.exe_command,
				callback: () => {this.saveImage(type.format)}
			});
		});

		return command_list;
	}

	public register(){
		this.types.forEach( (t)=>{
			this.context.subscriptions.push(vscode.commands.registerCommand(t.exe_command, () => {
				this.saveImage(t.format);
			}));
		});
	}

	//Generates an image for the selected justification diagram
	public async saveImage(format: Format): Promise<void> {
		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);

		// Execute the command, and wait for the result (must be synchronous).
		// TODO: Validate that this actually executes synchronously. 
		try{
			let command  = await this.save_image_command.makeCommand({ format: format, save_image: true});
			const {stdout, stderr} = await execPromise(command);

			ImageGenerator.output_channel.appendLine(stdout.toString());
			ImageGenerator.output_channel.appendLine(stderr.toString());
		} catch (error: any){
			ImageGenerator.output_channel.appendLine(error.toString());
		}
	}
}

type ImageType = {
	exe_command: string,
	format: Format
}

export enum Format{
	PNG = "PNG",
	SVG = "SVG",
}


