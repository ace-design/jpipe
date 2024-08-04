package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.UnknownSymbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public final class SymbolTable<T> {


    private final Map<String, T> symbols;

    public SymbolTable() {
        this.symbols = new HashMap<>();
    }

    public void record(String identifier, T element) {
        if (this.symbols.containsKey(identifier)) {
            throw new DuplicateSymbol(identifier);
        }
        this.symbols.put(identifier, element);
    }


    public T get(String identifier) {
        if (this.symbols.containsKey(identifier)){
            return this.symbols.get(identifier);
        }
        throw new UnknownSymbol(identifier);
    }

    public Collection<T> values() {
        return this.symbols.values();
    }


    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",","{","}");
        for (Map.Entry<String, T> entry: symbols.entrySet()) {
            joiner.add(entry.getKey() + " -->> " + entry.getValue().toString());
        }
        return joiner.toString();
    }
}
