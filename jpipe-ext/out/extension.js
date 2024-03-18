"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
const editorReader_1 = require("./editorReader");
// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
function activate(context) {
    context.subscriptions.push(editorReader_1.editorReader.register(context));
}
exports.activate = activate;
// const changeDocumentSubscription = vscode.window.onDidChangeActiveTextEditor(async e => {
// 	if (e !== undefined){
// 		vscode.window.showInformationMessage(e.document.toString())
// 	}
// });
// This method is called when your extension is deactivated
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map