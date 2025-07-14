package ca.mcscert.jpipe.model;

import java.util.*;

public class SymbolTree<T> {
    private SymbolNode<T> root;

    public SymbolTree() {
        this.root = null;
    }

    public SymbolTree(String identifier, T value) {
        this.root = new SymbolNode<>(identifier, value);
    }

    public SymbolNode<T> getRoot() {
        return root;
    }

    /** Find a node by identifier (DFS) */
    public SymbolNode<T> find(String identifier) {
        if (root == null) return null;
        return findRecursive(root, identifier);
    }

    private SymbolNode<T> findRecursive(SymbolNode<T> current, String identifier) {
        if (current.getIdentifier().equals(identifier)) {
            return current;
        }

        for (SymbolNode<T> child : current.getChildrenModels()) {
            SymbolNode<T> result = findRecursive(child, identifier);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /** Add a node under a parent identified by parentIdentifier */
    public boolean addNode(String parentIdentifier, String childIdentifier, T value) {
        SymbolNode<T> parent = find(parentIdentifier);
        if (parent == null) {
            return false; // parent not found
        }
        SymbolNode<T> newChild = new SymbolNode<>(childIdentifier, value);
        parent.addChild(newChild);
        return true;
    }

    /** Check if identifier exists in the tree */
    public boolean contains(String identifier) {
        return find(identifier) != null;
    }

    /** Find all nodes with the given identifier (DFS) */
    public List<SymbolNode<T>> findAll(String identifier) {
        List<SymbolNode<T>> matches = new ArrayList<>();
        if (root == null) return matches;
        findAllRecursive(root, identifier, matches);
        return matches;
    }

    private void findAllRecursive(SymbolNode<T> current, String identifier, List<SymbolNode<T>> matches) {
        if (current.getIdentifier().equals(identifier)) {
            matches.add(current);
        }

        for (SymbolNode<T> child : current.getChildrenModels()) {
            findAllRecursive(child, identifier, matches);
        }
    }

    /** Print the tree structure (for debugging) */
    public void printTree() {
        printTreeRecursive(root, 0);
    }

    private void printTreeRecursive(SymbolNode<T> node, int level) {
        if (node == null) return;

        System.out.println("  ".repeat(level) + "- " + node.getIdentifier() + ": " + node.getValue());
        for (SymbolNode<T> child : node.getChildrenModels()) {
            printTreeRecursive(child, level + 1);
        }
    }
}
