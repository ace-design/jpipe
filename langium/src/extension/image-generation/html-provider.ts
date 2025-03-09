export type VsCodeNodeMessage = {
    command: string
    text: string
    id: string
}

export class HTMLProvider{
    
    constructor(){
    }

    //gives HTML code given svg data
    public static getHtmlForWebview(svg_data: string): string {
        const svgContent = svg_data || ''; // Ensure svg_data is not null or undefined\

        return /* html */`
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>SVG Viewer</title>
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        background-color: #f0f0f0;
                    }
                    #svg-container {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        width: 100%;
                        height: 100%;
                    }
                    svg {
                        max-width: 100%;
                        max-height: 100%;
                        width: auto;
                        height: auto;
                    }
                </style>
            </head>
            <body>
                <div id="svg-container">
                    ${svgContent}
                </div>
                <script>
                `+HTMLProvider.getScript()+`
                </script>
            </body>
        </html>`;
    }

    //creates the loading HTMLWebview
    public static getLoadingHTMLWebview(): string {
		return `
		<!DOCTYPE html>
		<html lang="en">
		<head>
		  <meta charset="UTF-8">
		  <meta name="viewport" content="width=device-width, initial-scale=1.0">
		  <title>Loading Page</title>
		  <style>
			body {
			  margin: 0;
			  padding: 0;
			  position: relative; /* Add this line */
			  display: flex;
			  justify-content: center;
			  align-items: center;
			  height: 100vh;
			  background-color: #f0f0f0;
			}
			.loader {
			  position: absolute; /* Add this line */
			  top: 50%; /* Add this line */
			  left: 50%; /* Add this line */
			  transform: translate(-50%, -50%); /* Add this line */
			  border: 8px solid #f3f3f3;
			  border-top: 8px solid #3498db;
			  border-radius: 50%;
			  width: 50px;
			  height: 50px;
			  animation: spin 1s linear infinite;
			}
			@keyframes spin {
			  0% { transform: rotate(0deg); }
			  100% { transform: rotate(360deg); }
			}
		  </style>
		</head>
		<body>
		  <div class="loader"></div>
		</body>
		</html>		
		`
	}

    //helper function to create the script
    private static getScript(): string{
        return`
            const vscode = acquireVsCodeApi();

            // Registering event listeners for all nodes in the graph
            var nodes = Array.from(document.querySelectorAll('g .node'));
            nodes.forEach( function(n) { 
                n.addEventListener("click", function (e) {                 
                console.log("element clicked" + n.id); 
                handle_click(n.id); }) 
            })

        `+ HTMLProvider.getFunction()
        ;
    }

    //helper function to create the function within the script
    private static getFunction(): string{
        return `function handle_click(node_identifier) {
                let command = 'handle_click';
                let element = document.getElementById(node_identifier);
                let text = element.textContent;
                
                let message = {
                    command: command,
                    id: node_identifier,
                    text: text
                }


                vscode.postMessage(message)
            }`;
    }
}