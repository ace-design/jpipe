package ca.mcscert.jpipe.error;

/**
 *
 */
@JPipeError
public class DuplicateSymbol extends RuntimeException {

    private final String symbol;

    public DuplicateSymbol(String symbol) {
        super("Duplicated symbol [" + symbol + "]");
        this.symbol = symbol;
    }

}
