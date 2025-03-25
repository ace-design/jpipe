
const vscode = acquireVsCodeApi();
// Registering event listeners for all nodes in the graph
var nodes = Array.from(document.querySelectorAll('g .node'));
nodes.forEach( function(n) { 
    n.addEventListener("click", function (e) { handle_click(n.id); }) 
})
// Handling a click
function handle_click(node_identifier) {
    let command = 'handle_click';
    let element = document.getElementById(node_identifier);
    let text = element.textContent;
    
    let message = {
        command: command,
        id: node_identifier,
        text: text
    }
    vscode.postMessage(message)
}
// Changing backgroung based on provided id (call from console in chrome)
function highlight(node_identifier) {
    var node = document.getElementById(node_identifier);
    node.setAttribute("fill", "red");
}
