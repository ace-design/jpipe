import { AbstractExecuteCommandHandler, ExecuteCommandAcceptor } from "langium/lsp";
export class JpipeExecuteCommandHandler extends AbstractExecuteCommandHandler{
    override registerCommands(acceptor: ExecuteCommandAcceptor): void {

        acceptor("jpipe.sayHello",args => console.log("Jpipe says hello!"));
    }
}

