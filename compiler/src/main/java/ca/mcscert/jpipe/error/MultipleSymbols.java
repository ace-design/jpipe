package ca.mcscert.jpipe.error;

/**
 * Error thrown when multiple symbols are found under a given identifier.
 */
@JPipeError
public class MultipleSymbols extends RuntimeException {

    public MultipleSymbols(String identifier) {
        super("Multiple variables found under " + identifier + ", Please specify scope");
    }

}
