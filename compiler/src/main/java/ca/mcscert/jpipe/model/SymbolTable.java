package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.UnknownSymbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Define a scope inside a model element, binding local symbols (strings) to elements of type T.
 *
 * <p>Example: a given justification contains a symbol table of JustificationElements.</p>
 *
 * @param <T> the type of elements stored in the
 */
public final class SymbolTable<T> {

    private final Map<String, T> symbols;

    public SymbolTable() {
        this.symbols = new HashMap<>();
    }

    /**
     * Record a new symbol in the symbol table.
     *
     * @param identifier the symbol's identifier.
     * @param element the element to record.
     * @throws DuplicateSymbol is this symbol is already used in the table.
     */
    public void record(String identifier, T element) throws DuplicateSymbol {
        if (this.symbols.containsKey(identifier)) {
            throw new DuplicateSymbol(identifier);
        }
        this.symbols.put(identifier, element);
    }

    /**
     * Get the element associated to a given symbol inside the table.
     *
     * @param identifier the identifier one is looking for.
     * @return the stored element, if any.
     * @throws UnknownSymbol if the symbol is not defined in the table.
     */
    public T get(String identifier) throws UnknownSymbol {
        if (this.symbols.containsKey(identifier)) {
            return this.symbols.get(identifier);
        }
        throw new UnknownSymbol(identifier);
    }

    /**
     * Return all the elements recorded inside the table.
     *
     * @return a collection of elements.
     */
    public Collection<T> values() {
        return this.symbols.values();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (Map.Entry<String, T> entry : symbols.entrySet()) {
            joiner.add(entry.getKey() + " -->> " + entry.getValue().toString());
        }
        return joiner.toString();
    }
}
