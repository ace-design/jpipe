package ca.mcscert.jpipe.error;

import java.util.List;

@JPipeError
public class MultipleSymbols extends RuntimeException {
    public MultipleSymbols(List<String> symbols) {
        super("Multiple symbols found [" + symbols + "], Please specify scopes");
    }
}
