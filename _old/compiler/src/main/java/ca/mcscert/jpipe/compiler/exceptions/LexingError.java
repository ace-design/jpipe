package ca.mcscert.jpipe.compiler.exceptions;

/**
 * custom type to identify compilation error identified while running the compiler.
 */
public class LexingError extends RuntimeException {

    public int line;
    public int column;

    /**
     * constructor for a compilation error.
     *
     * @param l the line where the error was identified.
     * @param c the column where the error was identified.
     * @param message the compilation error message
     */
    public LexingError(int l, int c, String message) {
        super("[" + l +  ":" + c + "] " + message);
        this.column = c;
        this.line = l;
    }



}
