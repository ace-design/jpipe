package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Define what a compilation Unit is in JPipe.
 */
public final class Unit implements Visitable {

    private final String name;
    private final SymbolTable<Justification> contents;
    private final Set<String> loaded;

    /**
     * Instantiates a compilation unit, using its name (source file).
     *
     * @param name the source file used to run the compiler.
     * @param filePath path of the original file;
     */
    public Unit(String name, Path filePath) {
        this.name = name;
        this.contents = new SymbolTable<>();
        this.loaded = new HashSet<>();
        loaded.add(filePath.normalize().toString());
    }

    public String getName() {
        return name;
    }


    public boolean isInScope(Path p) {
        return this.loaded.contains(p.normalize().toString());
    }

    /**
     * Add a path as part of the files loaded to contribute to this unit.
     *
     * @param p the path to add
     */
    public void addLoadedFile(Path p) {
        this.loaded.add(p.normalize().toString());
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


    @Override
    public String toString() {
        return "Unit{"
                + "name='" + name + '\''
                + ", contents=" + contents
                + ", loaded=" + loaded
                + '}';
    }
}
