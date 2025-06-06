package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.elements.JustificationElement;

public final class SymbolRow<T> {
    private String Identifier;
    private T element;
    private Long parent;

    public SymbolRow(String identifier, T element, Long parent) {
        Identifier = identifier;
        this.element = element;
        this.parent = parent;
    }

    public SymbolRow(String identifier, T element) {
        Identifier = identifier;
        this.element = element;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
}
