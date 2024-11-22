import { ExecException } from "child_process";
import * as env from "./abstract-environment.js";

//A graphviz environment check which runs on lsp startup
export class GraphvizEnvironmentCheck extends env.AbstractEnvironmentCheck{
    public override command: string;

    constructor(){
        super();
        this.command = "dot -V";
    }

    //Returns the resultant message and command type for an error
    protected override ErrorEnvironmentResult(error: ExecException | null): env.EnvironmentCheckResult {   
        return {
            command: env.EnvironmentCommand.ERROR_MESSAGE,
            message: "Graphviz is not installed or not in the PATH. Please install Graphviz before using Jpipe"
        };
    }

    //Returns the resultant message and command type for information (non-error)
    protected override InformationEnvironmentResult(info: string): env.EnvironmentCheckResult {
        return{
            command: env.EnvironmentCommand.INFORMATION_MESSAGE,
            message: `Graphviz is installed:\n${info}`
        }
    }
    
}