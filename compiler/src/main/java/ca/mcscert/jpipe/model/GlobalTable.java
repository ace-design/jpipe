package ca.mcscert.jpipe.model;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalTable {
    // The single instance
    private static GlobalTable instance = null;

    // The map (table) to store key-value pairs
    private final Map<Long, Pair<Long, List<Long>>> table;

    // Private constructor to prevent instantiation
    private GlobalTable() {
        table = new HashMap<>();
    }

    // Method to get the single instance
    public static synchronized GlobalTable getInstance() {
        if (instance == null) {
            instance = new GlobalTable();
        }
        return instance;
    }

    // Methods to interact with the table
    public void putall(Long key, Pair<Long, List<Long>> value) {
        table.put(key, value);
        updateParent(value.getLeft(), key);

    }

    public Pair<Long,List<Long>> getall(Long key) {
        return table.get(key);
    }

    public void addChild(Long key, Long child) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            value.getRight().add(child);
            table.put(key, value);
        }
        else {
            throw new RuntimeException("Value for key " + key + " is not found");
        }

    }

    public void removeChild(Long key, Long child) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            value.getRight().remove(child);
            table.put(key, value);
        }
        else {
            throw new RuntimeException("Value for key " + key + " is not found");
        }
    }

    public List<Long> getChilds(Long key) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            return value.getRight();
        }
        throw new RuntimeException("Value for key " + key + " is not found");
    }

    public void setParent(Long key, Long parent) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            table.put(key, ImmutablePair.of(parent, value.getRight()));
        }
        else {
            throw new RuntimeException("Value for key " + key + " is not found");
        }
    }

    public Long getParent(Long key) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            return value.getLeft();
        }
        throw new RuntimeException("Value for key " + key + " is not found");
    }

    public boolean containsKey(Long key) {
        return table.containsKey(key);
    }

    public boolean containsChildForKey(Long key, Long child) {
        Pair<Long, List<Long>> value = table.get(key);
        if (value != null) {
            return value.getRight().contains(child);
        }
        return false;
    }

    public void remove(Long key) {
        table.remove(key);
    }

    public void clear() {
        table.clear();
    }

    // Concurrency and logic functions

    private void updateParent(Long key, Long childKey) {
        List<Long> children = table.get(key).getRight();
        children.add(childKey);
        table.put(key, ImmutablePair.of(table.get(key).getLeft(), children));
    }


}
