package ca.mcscert.jpipe.compiler;

public class CompilationError extends RuntimeException {

    public int line;
    public int column;

    public CompilationError(int l, int c, String message) {
        super("[" + l +  ":" + c + "] " + message);
        this.column = c;
        this.line = l;
    }



}
