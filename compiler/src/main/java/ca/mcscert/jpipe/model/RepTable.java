package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.MultipleSymbols;
import ca.mcscert.jpipe.error.UnknownSymbol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The {@code RepTable} class represents a simple symbolic mapping
 * structure that tracks parent-child relationships between elements
 * of a generic type {@code T}. This class supports recording,
 * retrieving, and deleting elements in a hierarchical structure.
 * Internally, each parent maps to a list of its direct children.
 * All elements (parents and children) are tracked via a node set.
 *
 * @param <T> the type of the elements managed by this representation table
 */
public final class RepTable<T> {

    private final Map<T, Set<T>> table;
    private final Set<T> allNodes;

    /**
     * Constructs an empty representation table.
     */
    public RepTable() {
        table = new HashMap<>();
        allNodes = new HashSet<>();
    }

    /**
     * Checks whether the table contains a given element.
     *
     * @param key the element to check for presence
     * @return {@code true} if the element exists, {@code false} otherwise
     */
    public boolean containsKey(T key) {
        return allNodes.contains(key);
    }

    /**
     * Records a new child-parent relationship in the representation table.
     *
     * @param current the element to record as a child
     * @param parent  the parent of the element
     * @throws DuplicateSymbol if the element is already recorded
     */
    public void record(T current, T parent) {
        allNodes.add(current);
        table.computeIfAbsent(parent, k -> new HashSet<>()).add(current);

        if (parent != null) {
            allNodes.add(parent);
        }
    }

    /**
     * Records a new root element (with no parent) in the representation table.
     *
     * @param current the element to record
     * @throws DuplicateSymbol if the element is already recorded
     */
    public void record(T current) throws DuplicateSymbol {
        if (table.containsKey(current)) {
            throw new DuplicateSymbol(current.toString());
        }
        allNodes.add(current);
        table.putIfAbsent(current, new HashSet<>());
    }

    /**
     * Records a new root element (with no parent) in the representation table.
     *
     * @param current the element to record
     * @throws DuplicateSymbol if the element is already recorded
     */
    public void recordAll(RepTable<T> current) {
        this.table.putAll(current.table);
        this.allNodes.addAll(current.allNodes);
    }

    /**
     * Deletes an element from the table and reassigns its children as roots.
     *
     * @param current the element to delete
     * @throws UnknownSymbol if the element does not exist
     */
    public void delete(T current) {
        if (!containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }
        safeDelete(current);
    }

    /**
     * Retrieves the direct parent of a given element.
     *
     * @param child the element whose parent is to be found
     * @return the parent of the element, or {@code null} if it's a root
     * @throws UnknownSymbol if the element is not found
     */
    public T getParent(T child) {
        for (Map.Entry<T, Set<T>> entry : table.entrySet()) {
            if (entry.getValue().contains(child)) {
                return entry.getKey();
            }
        }
        if (table.containsKey(child)) {
            return null;
        }
        throw new UnknownSymbol(child.toString());
    }

    /**
     * Recursively finds the original ancestor (root) of the given element.
     *
     * @param current the element whose original parent is needed
     * @return the root ancestor of the element
     * @throws UnknownSymbol if the element does not exist or a cycle is detected
     */
    public T getOriginalParent(T current) {
        return recursiveGetParent(current, new HashSet<>());
    }

    /**
     * Helper method for recursively retrieving the root ancestor.
     *
     * @param current the current element
     * @param visited set to track visited nodes and detect cycles
     * @return the root ancestor
     * @throws UnknownSymbol if the element does not exist or a cycle is found
     */
    private T recursiveGetParent(T current, Set<T> visited) {
        if (!containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }
        if (visited.contains(current)) {
            throw new UnknownSymbol("Cycle detected for symbol: " + current.toString());
        }

        T parent = getParent(current);
        if (parent == null) {
            return current;
        }

        visited.add(current);
        return recursiveGetParent(parent, visited);
    }

    /**
     * Safely deletes an element from the table by promoting its children to roots
     * and cleaning up references from its parent.
     *
     * @param current the element to safely delete
     * @throws UnknownSymbol if the element does not exist
     */
    private void safeDelete(T current) {
        if (!containsKey(current)) {
            throw new UnknownSymbol(current.toString());
        }

        Set<T> children = table.getOrDefault(current, new HashSet<>());
        for (T child : children) {
            table.computeIfAbsent(null, k -> new HashSet<>()).add(child);
        }

        T parent = getParent(current);
        if (parent != null) {
            table.get(parent).remove(current);
        }

        table.remove(current);
        allNodes.remove(current);
    }

    /**
     * Returns the internal representation table mapping parents to their children.
     *
     * @return the underlying map of the table
     */
    public Map<T, Set<T>> getTable() {
        return table;
    }

    /**
     * Retrieves all the parents of a child element.
     *
     * @param child the element whose parent is to be found
     * @return set of parents of the element, or {@code null} if it's a root
     * @throws UnknownSymbol if the element is not found
     */
    public Set<T> getAllParents(T child) {
        Set<T> parents = new HashSet<>();
        for (Map.Entry<T, Set<T>> entry : table.entrySet()) {
            if (entry.getValue().contains(child)) {
                parents.add(entry.getKey());
            }
        }
        if (!parents.isEmpty()) {
            return parents;
        } else if (table.containsKey(child)) {
            return null;
        }
        throw new UnknownSymbol(child.toString());
    }
}
