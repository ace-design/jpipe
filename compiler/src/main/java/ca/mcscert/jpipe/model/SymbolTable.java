package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.DuplicateSymbol;
import ca.mcscert.jpipe.error.MultipleSymbols;
import ca.mcscert.jpipe.error.UnknownSymbol;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Define the hierarchical status of each Justification Element by their given string identifier.
 * Each Justification Model have a symbol tree to contain
 * all their own Justification Elements with their respective scope.
 * A single identifier has a one-to-many relationship with Justification Elements.
 *
 */
public final class SymbolTable {

    // symbol table
    private final Map<String, Set<JustificationElement>> elements;
    // representation table
    private final HierarchyMap<JustificationElement> hierarchyMap;

    public SymbolTable() {
        this.elements = new HashMap<>();
        this.hierarchyMap = new HierarchyMap<>();
    }

    /**
     * Records an identifier and its related justification element.
     *
     * @param identifier String identifier
     * @param element Justification element
     */
    public void record(String identifier, JustificationElement element) throws DuplicateSymbol {
        recordingToTree(identifier, element);
        this.hierarchyMap.record(element);
    }

    /**
     * Records an identifier and its related justification element.
     *
     * @param identifier String identifier
     * @param element Justification element
     */
    public void record(String identifier, JustificationElement element,
                       JustificationElement rep) throws DuplicateSymbol {
        recordingToTree(identifier, element);
        this.hierarchyMap.record(element, rep);
    }

    /**
     * Records an identifier and its related justification element.
     *
     * @param identifier String identifier
     * @param element Justification element
     */
    public void record(String identifier, JustificationElement element,
                       Set<JustificationElement> reps) throws DuplicateSymbol {
        recordingToTree(identifier, element);
        this.hierarchyMap.record(element, reps);
    }

    private void recordingToTree(String identifier, JustificationElement element) {
        if (this.keys().contains(identifier)) {
            for (JustificationElement je : this.elements.get(identifier)) {
                if (je.equals(element)) {
                    throw new DuplicateSymbol(identifier);
                }
            }
            this.elements.get(identifier).add(element);
        } else {
            this.elements.put(identifier, new HashSet<>());
            this.elements.get(identifier).add(element);
        }
    }

    /**
     * Deletes an identifier and the given justification element.
     *
     * @param identifier String identifier
     * @throws UnknownSymbol if element is not found in the SymbolTree
     */
    public void delete(String identifier, JustificationElement element) {
        if (this.keys().contains(identifier)) {
            for (JustificationElement je : this.elements.get(identifier)) {
                if (je.equals(element)) {
                    this.elements.get(identifier).remove(je);
                    if (this.elements.get(identifier).isEmpty()) {
                        this.elements.remove(identifier);
                    }
                    this.hierarchyMap.delete(element);
                    return;
                }
            }
        }
        throw new UnknownSymbol(identifier);
    }

    /**
     * Get the element associated to a given symbol inside the table.
     *
     * @param identifier the identifier one is looking for.
     * @return the stored element, if any.
     * @throws UnknownSymbol if the symbol is not defined in the table.
     */
    public JustificationElement get(String identifier) throws UnknownSymbol {
        if (identifier.contains(":")) {
            String[] parts = identifier.split(":");
            String id = parts[parts.length - 1];
            return this.get(id, identifier);
        }

        if (this.elements.containsKey(identifier)) {
            if (this.elements.get(identifier).size() == 1) {
                return this.elements.get(identifier).iterator().next();
            }
            throw new MultipleSymbols(identifier);
        }
        throw new UnknownSymbol(identifier);
    }

    /**
     * Get the element associated to a given symbol inside the table.
     *
     * @param identifier the identifier one is looking for.
     * @return the stored element, if any.
     * @throws UnknownSymbol if the symbol is not defined in the table.
     */
    public JustificationElement get(String identifier, String scope) throws UnknownSymbol {
        if (this.elements.containsKey(identifier)) {
            JustificationElement je = null;
            for (JustificationElement e : this.elements.get(identifier)) {
                if (e.getScope().equals(scope)) {
                    return e;
                }
                if (e.getScope().contains(scope)) {
                    if (je != null) {
                        throw new MultipleSymbols(identifier);
                    }
                    je = e;
                }
            }
            if (je != null) {
                return je;
            }
        }
        throw new UnknownSymbol(identifier);
    }

    /**
     * Return all the elements recorded inside the table.
     *
     * @return a collection of elements.
     */
    public Collection<JustificationElement> values() {
        return elements.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    /**
     * Compute the set of keys defined in this symbol table.
     *
     * @return The symbols, as a set of strings.
     */
    public Set<String> keys() {
        return new HashSet<>(this.elements.keySet());
    }

    /**
     * Check if a given symbol exists inside the table.
     *
     * @param identifier the symbol to look for.
     * @return true if it exists, false elsewhere.
     */
    public boolean exists(String identifier) {
        return this.elements.containsKey(identifier);
    }

    public HierarchyMap<JustificationElement> getHierarchyMap() {
        return hierarchyMap;
    }

    public boolean containsHierachyKey(JustificationElement element) {
        return this.hierarchyMap.containsKey(element);
    }


}
