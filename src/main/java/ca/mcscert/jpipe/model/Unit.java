package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.HashSet;
import java.util.Set;

public class Unit implements Visitable {

    private final String fileName;
    private final Set<Justification> justificationSet;

    public Unit(String fileName) {
        this.fileName = fileName;
        this.justificationSet = new HashSet<>();
    }

    public void add(Justification justification) {
        this.justificationSet.add(justification);
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    public String getFileName() {
        return fileName;
    }

    public Set<Justification> getJustificationSet() {
        return justificationSet;
    }
}
