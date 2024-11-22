import { exec, ExecException } from "child_process";

export type EnvironmentCheckResult = {
    command:  EnvironmentCommand,
    message: string
}

export enum EnvironmentCommand{
    ERROR_MESSAGE,
    INFORMATION_MESSAGE
}

//Abstract definition of an environment check which runs on lsp startup
export abstract class AbstractEnvironmentCheck{
    public abstract command: string;

    //Run the environment check and return the resultant message and command type for the environment manager
    public async checkEnvironment(): Promise<EnvironmentCheckResult>{
        return new Promise<EnvironmentCheckResult>((resolve, reject) => {
            exec(this.command, (error, stdout, stderr) => {
                if (error) {
                    resolve(this.ErrorEnvironmentResult(error));
                } else {
                    resolve(this.InformationEnvironmentResult(stderr || stdout));
                }
            });
        });
    };

    //Returns the resultant message and command type for an error
    protected abstract ErrorEnvironmentResult(error: ExecException | null): EnvironmentCheckResult;

    //Returns the resultant message and command type for information (non-error)
    protected abstract InformationEnvironmentResult(info: string): EnvironmentCheckResult;
}