package ca.mcscert.jpipe.compiler.exceptions;

/**
 * Custom class to identify typing error at runtime while compiling.
 */
public class TypeError extends RuntimeException {

    private String scopeIdentifier;

    public TypeError(String message, String scopeIdentifier) {
        super("[TypeSystem] " + message);
        this.scopeIdentifier = scopeIdentifier;
    }

    public String getScopeIdentifier() {
        return scopeIdentifier;
    }
}
