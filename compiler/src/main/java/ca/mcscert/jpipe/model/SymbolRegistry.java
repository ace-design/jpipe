package ca.mcscert.jpipe.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Help localizing a symbol. (singleton)
 */
public class SymbolRegistry {

    /**
     * Provide access to the global (singleton) registry.
     *
     * @return the singletoned instance.
     */
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


    /**
     * Record a symbol inside the registry.
     *
     * @param scope the scope containing the symbol (null if global)
     * @param name the name of the symbol
     * @param file the file containing the symbol
     * @param line line where the symbol was declared
     * @param column column where the symbol was declared
     */
    public void record(String scope, String name, String file, int line, int column) {
        scope = (scope == null ? NO_SCOPE : scope);
        String symbolName = scope + ":" + name;
        this.lookup.put(symbolName, new Symbol(symbolName, file, line, column));
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
