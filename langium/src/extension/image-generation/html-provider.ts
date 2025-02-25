// import * as vscode from 'vscode';
// import { JpipeFileSystemManager } from '../managers/index.js';

// export class HTMLProvider{
    
//     constructor(fs: JpipeFileSystemManager){
//         let scriptURI = vscode.Uri.file("src/extension/image-generation/preview_script.js");
//         let text = vscode.workspace.fs.readFile(scriptURI)
//         let document_text = text.then(result => {
//             return result.toString()
//         })
//     }
//     private static getHtmlForWebview(svg_data: string): string {
//         const svgContent = svg_data || ''; // Ensure svg_data is not null or undefined\

//         return /* html */`
//             <!DOCTYPE html>
//             <html lang="en">
//             <head>
//                 <meta charset="UTF-8">
//                 <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                 <title>SVG Viewer</title>
//                 <style>
//                     body {
//                         margin: 0;
//                         padding: 0;
//                         display: flex;
//                         justify-content: center;
//                         align-items: center;
//                         height: 100vh;
//                         background-color: #f0f0f0;
//                     }
//                     #svg-container {
//                         display: flex;
//                         justify-content: center;
//                         align-items: center;
//                         width: 100%;
//                         height: 100%;
//                     }
//                     svg {
//                         max-width: 100%;
//                         max-height: 100%;
//                         width: auto;
//                         height: auto;
//                     }
//                 </style>
//             </head>
//             <body>
//                 <div id="svg-container">
//                     ${svgContent}
//                 </div>
//             </body>
//             </html>
//         `;
//     }

//     private static getScript(): string{
//         return`
//         <script>
//             const vscode = acquireVsCodeApi();

//         `+ HTMLProvider.getFunction()+`
//         </script>`
//         ;
//     }

//     private static getFunction(): string{
//         return `function goToDefintion(elementID) {
//                 let element = document.getElementByID(elementID)
//                 vscode.postMessage({ command: 'goToDefinition', elementText: 'Circle was clicked' });
//             }
//         `;
//     }
// }