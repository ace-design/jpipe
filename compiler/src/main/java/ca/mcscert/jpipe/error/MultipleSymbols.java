package ca.mcscert.jpipe.error;

import java.util.List;

@JPipeError
public class MultipleSymbols extends RuntimeException {

    public MultipleSymbols(String identifier) {
        super("Multiple variables found under " + identifier + ", Please specify scope");
    }

}
