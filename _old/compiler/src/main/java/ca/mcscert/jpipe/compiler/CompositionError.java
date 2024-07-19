package ca.mcscert.jpipe.compiler;

/**
 * Runtime exception thrown when encountering a composition error.
 */
public class CompositionError extends RuntimeException {

    public CompositionError(String message) {
        super("[CompositionError] " + message);
    }
}
