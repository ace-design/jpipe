package ca.mcscert.jpipe.compiler;

/**
 * Custom class to identify typing error at runtime while compiling.
 */
public class TypeError extends RuntimeException {

    public TypeError(String message) {
        super("[TypeSystem] " + message);
    }
}
