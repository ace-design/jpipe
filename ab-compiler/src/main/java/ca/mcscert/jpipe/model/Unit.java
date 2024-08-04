package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.util.Collection;

public final class Unit implements Visitable {

    private final String name;
    private final SymbolTable<Justification> contents;

    public Unit(String name) {
        this.name = name;
        this.contents = new SymbolTable<>();
    }

    public String getName() {
        return name;
    }

    public void add(Justification j) {
        this.contents.record(j.getName(), j);
    }

    public void addInto(String container, JustificationElement e) {
        Justification justification = contents.get(container);
        justification.add(e);
    }

    public Justification get(String identifier) {
        return this.contents.get(identifier);
    }

    public Collection<Justification> getContents() {
        return this.contents.values();
    }


    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }

}
