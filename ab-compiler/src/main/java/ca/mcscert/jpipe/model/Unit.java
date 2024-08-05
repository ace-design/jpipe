package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.util.Collection;

/**
 * Define what a compilation Unit is in JPipe.
 */
public final class Unit implements Visitable {

    private final String name;
    private final SymbolTable<Justification> contents;

    /**
     * Instantiates a compilation unit, using its name (source file).
     *
     * @param name the source file used to run the compiler.
     */
    public Unit(String name) {
        this.name = name;
        this.contents = new SymbolTable<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Record a new justification inside the unit symbol table.
     *
     * @param j the justification to record.
     */
    public void add(Justification j) {
        this.contents.record(j.getName(), j);
    }

    /**
     * Add a justification element inside a justification.
     *
     * @param container the justification to contain the element.
     * @param e the element to add.
     */
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
