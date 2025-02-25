const vscode = acquireVsCodeApi();

function goToDefintion(elementID) {
    let element = document.getElementByID(elementID);

    vscode.postMessage({ command: 'goToDefinition', elementText: element.elementText })
}
