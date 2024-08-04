package ca.mcscert.jpipe.error;

@JPipeError
public class UnknownSymbol extends RuntimeException {

    private final String symbol;
    private final String location;

    public UnknownSymbol(String symbol, String location) {
        super("Unknown symbol ["+symbol+"] (location: " + location + ")");
        this.symbol = symbol;
        this.location = location;
    }

    public UnknownSymbol(String symbol) {
        super("Unknown symbol ["+symbol+"] (location: ?");
        this.symbol = symbol;
        this.location = null;
    }

}
