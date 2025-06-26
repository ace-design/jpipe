package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.UnknownSymbol;
import ca.mcscert.jpipe.model.elements.JustificationElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class RepTable<T> {

    private final HashMap<T, T> table;

    public RepTable() {
        table = new HashMap<T, T>();
    }

    public boolean containsKey(T key){
        return table.containsKey(key);
    }

    public void record(T current, T parent) {
        if (table.containsKey(current)) {
            throw new DuplicateSymbol(current.toString());
        }
        else {
            table.put(current, parent);
        }
    }

    public void record(T current) {
        if (table.containsKey(current)) {
            throw new DuplicateSymbol(current.toString());
        }
        else {
            table.put(current, null);
        }
    }

    // Need to add a safe delete option
    // Deletion of an item would lead to a newer parent element
    public void delete(T current) {
        if (!table.containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }
        safeDelete(current);
    }

    public T getParent(T current) {
        if (table.containsKey(current)) {
            return table.get(current);
        }
        throw new UnknownSymbol(current.toString());
    }

    public T getOriginalParent(T current) {
        return recursiveGetParent(current, new HashSet<>());
    }

    private T recursiveGetParent(T current, Set<T> taken) {

        if(!table.containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }
        if(taken.contains(current)) {
            throw new UnknownSymbol(current.toString());
        }

        T parentValue = table.get(current);
        if(parentValue==null){
            return current;
        }
        taken.add(current);
        return recursiveGetParent(parentValue, taken);

    }

    public void safeDelete(T current) {
        if (!table.containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }

        // Update any children whose parent is the current element
        for (T key : table.keySet()) {
            if (current.equals(table.get(key))) {
                table.put(key, null);  // Promote child to root (no parent)
            }
        }

        // Finally, remove the element
        table.remove(current);
    }

    public HashMap<T, T> getTable() {
        return table;
    }




}
