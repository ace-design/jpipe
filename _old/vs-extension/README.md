# Visualizer for JPipe - VSCode Extension

This visualizer was developed using the extension api provided by vscode: https://code.visualstudio.com/api


## Table of Contents

- Features
- Installation
- Usage
- Contributions

## Features

The extension is composed of two core features:
1. Visualizer for the jpipe language. It allows users to preview their code, and view real time changes upon changing the code. The visualizer is shown on a global webview window on the right of the jpipe code. If a file contains multiple diagrams, clicking on the desired diagram in the text editor will allow users to visualize that diagram.
2. LSP used for syntax highlighting. LSP was developed by Alexandre Lachance. See the following for more details:
- https://github.com/ace-design/lever-framework, 
- https://github.com/ace-design/p4-lsp

## Installation

Install the extension into your vscode environment by using the `.vsix` binary generated after packaging the code:

``code --install-extension <file name>.vsix``

While testing, and debugging, you may need to manually remove the extension from `.vscode/extensions` on your local computer. Since vscode may cache previous extension even after installation. 

## Usage
![extension_example](images/extension_example.png)
1. Run the extension by opening any *.jd file. The extension will automatically open the text editor on the left with lsp syntax highlighting, and visualizer on the right. 
2. Change diagrams within files by click on any other diagram within the text editor. 
3. Opening another file will update the webview accordingly if it is also a *.jd file. 
4. Making changes to any diagrams will regenerate the updated diagram on the right.
5. For compilationChain output, go to the output tabe, and look under "jpipe_console".



## Contributions

Contributions can be made by modifying the code in the [src/](src/) directory.
1. The code for the visualizer can be found in the [src/editorReader.ts](src/editorReader.ts) file. 
2. The code for the LSP, and visualizer registration can be found in the [src/extension.ts](src/editorReader.ts) file. Note, this is the main entry point for the extension. 
3. If changes are made to the LSP, then regenerate the binary and replace [jpipe-language-server](jpipe-language-server) with it. 
4. Similarly, if changes are made to the jpipe compilationChain, then regenerate the jar file, and replace [jpipe.jar](jpipe.jar) with it. 
5. Regenerate the `.vsix` file with updated changes using the following command in the main directory:
    `vsce package`

*Note: For testing while coding, just run the extension manually rather than packaing it each time. Packaging should only be done if testing a release of the extension.*

