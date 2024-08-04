package ca.mcscert.jpipe.error;

/**
 * Custom exception used to stop the compilation process through the error logger manager.
 *
 * @author mosser
 */
public class FatalException extends RuntimeException {

    public FatalException() {
        super();
    }

    public FatalException(String message) {
        super(message);
    }
}
