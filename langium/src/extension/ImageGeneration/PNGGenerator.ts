import * as vscode from 'vscode';
import { ImageGenerator,Format } from './ImageGenerator.js';

export class pngGenerator extends ImageGenerator{
	protected exe_command: string;
	protected format: Format;

    constructor( context: vscode.ExtensionContext) {
		super(context);
		this.exe_command = "jpipe.downloadPNG";
		this.format = Format.PNG;
	}

}