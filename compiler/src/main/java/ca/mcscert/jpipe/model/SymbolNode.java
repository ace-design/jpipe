package ca.mcscert.jpipe.model;

import java.util.HashSet;
import java.util.Set;

public class SymbolNode<T> {
    String identifier;
    T value;
    Set<SymbolNode<T>> childrenModels;

    public SymbolNode(String identifier, T value) {
        this.identifier = identifier;
        this.value = value;
        this.childrenModels = new HashSet<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public T getValue() {
        return value;
    }

    public Set<SymbolNode<T>> getChildrenModels() {
        return childrenModels;
    }

    public void addChild(SymbolNode<T> child) {
        this.childrenModels.add(child);
    }

    // Helper toString for debugging
    @Override
    public String toString() {
        return "SymbolNode{" +
                "identifier='" + identifier + '\'' +
                ", value=" + value +
                '}';
    }
}
