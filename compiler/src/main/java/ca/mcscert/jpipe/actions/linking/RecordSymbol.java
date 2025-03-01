package ca.mcscert.jpipe.actions.linking;

import ca.mcscert.jpipe.actions.RegularAction;
import ca.mcscert.jpipe.model.SymbolRegistry;
import ca.mcscert.jpipe.model.Unit;

/**
 * Record a terminal symbol in the shared symbol locator registry.
 */
public class RecordSymbol extends RegularAction {

    private final String scope;
    private final String symbol;
    private final String filename;
    private final int line;
    private final int col;

    /**
     * Record a symbol.
     *
     * @param scope the scope of the symbol (null if global)
     * @param symbol the symbol name
     * @param filename the file containing the declaration
     * @param line line where the symbol was declared
     * @param col column where the symbol was declared
     */
    public RecordSymbol(String scope, String symbol, String filename, int line, int col) {
        this.scope = scope;
        this.symbol = symbol;
        this.filename = filename;
        this.line = line;
        this.col = col;
    }

    @Override
    public void execute(Unit context) throws Exception {
        SymbolRegistry.getInstance().record(scope, symbol, filename, line, col);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RecordSymbol{");
        sb.append("scope='").append(scope).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", filename='").append(filename).append('\'');
        sb.append(", line=").append(line);
        sb.append(", col=").append(col);
        sb.append('}');
        return sb.toString();
    }
}
