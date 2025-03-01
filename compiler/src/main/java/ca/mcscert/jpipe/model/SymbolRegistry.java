package ca.mcscert.jpipe.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Help localizing a symbol. (singleton)
 */
public class SymbolRegistry {

    public static SymbolRegistry getInstance() {
        if (instance == null) {
            instance = new SymbolRegistry();
        }
        return instance;
    }

    private static final String NO_SCOPE = "<global>";
    private static SymbolRegistry instance;

    private final Map<String, Symbol> lookup;

    private SymbolRegistry() {
        this.lookup = new HashMap<>();
    }


    public void record(String scope, String name, String file, int line, int column) {
        scope = (scope == null ? NO_SCOPE : scope);
        String symbolName = scope + ":" + name;
        this.lookup.put(symbolName, new Symbol(symbolName, file, line, column));
    }

    public String rename(String oldSymbol) {
        if (! lookup.containsKey(oldSymbol)) {
            throw new IllegalArgumentException("Cannot rename non-existing symbol ["
                                                    + oldSymbol + "]");
        }
        String newSymbol = UUID.randomUUID().toString();
        this.lookup.put(newSymbol, this.lookup.get(oldSymbol));
        return newSymbol;
    }

    public Symbol lookup(String symbol) {
        if (this.lookup.containsKey(symbol)) {
            return lookup.get(symbol); // terminal declaration
        }
        throw new IllegalArgumentException("Unknown symbol [" + symbol + "]");
    }

    /**
     * Define what a symbol is.
     */
    @SuppressWarnings({"checkstyle:LeftCurly", "checkstyle:RightCurly"})
    public record Symbol(String symbolName, String file, int line, int column) {
        @Override public String toString() {
            String tpl = "%s [%d:%d] @%s";
            return String.format(tpl, symbolName, line, column, file);
        }

        public String name() { return this.symbolName; }
    }
}
