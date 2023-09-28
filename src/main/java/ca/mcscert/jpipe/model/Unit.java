package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Unit implements Visitable {

    private final String fileName;
    private final Set<JustificationDiagram> justificationSet;

    public Unit(String fileName) {
        this.fileName = fileName;
        this.justificationSet = new HashSet<>();
    }


    public Optional<JustificationDiagram> findByName(String name) {
        for (JustificationDiagram j : this.getJustificationSet()) {
            if (j.name().equals(name))
                return Optional.of(j);
        }
        return Optional.empty();
    }

    public void add(JustificationDiagram justification) {
        this.justificationSet.add(justification);
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    public String getFileName() {
        return fileName;
    }

    public Set<JustificationDiagram> getJustificationSet() {
        return justificationSet;
    }

    public void merge(Unit unit) {
        for (JustificationDiagram j : unit.getJustificationSet())
        {
            this.justificationSet.add(j);
        }
    }
}
