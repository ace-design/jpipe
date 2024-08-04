import * as vscode from 'vscode';
import util from "node:util";
import { SaveImageCommand } from './save-image-command.js';
import { Command, CommandUser } from '../managers/command-manager.js';

export class ImageGenerator implements CommandUser{
	// New channel created in vscode terminal for user debugging.
	private output_channel: vscode.OutputChannel;

	// Defines the command needed to execute the extension. 
	private save_image_command: SaveImageCommand;
	
	//possible image types and associated commands
	private types: ImageType[]; 
    
	constructor(save_image_command: SaveImageCommand, output_channel: vscode.OutputChannel) {
		this.save_image_command = save_image_command;
		this.output_channel = output_channel;
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

	//used to register commands with command manager
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

	//Generates an image for the selected justification diagram
	public async saveImage(format: Format): Promise<void> {
		const { exec } = require('node:child_process');
		const execPromise = util.promisify(exec);

		// Execute the command, and wait for the result (must be synchronous).
		// TODO: Validate that this actually executes synchronously. 
		try{
			let command  = await this.save_image_command.makeCommand({ format: format, save_image: true});
			const {stdout, stderr} = await execPromise(command);

			this.output_channel.appendLine(stdout.toString());
			this.output_channel.appendLine(stderr.toString());
		} catch (error: any){
			this.output_channel.appendLine(error.toString());
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

