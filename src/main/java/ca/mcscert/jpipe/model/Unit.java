package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represent a compilation unit (the contents of a file, and its included ones).
 * Elements that require resolution (e.g., load, weave) are resolved in the unit.
 */
public class Unit implements Visitable {

    // set of justification diagrams element defined in the file
    private final Set<JustificationDiagram> justificationSet;

    public Unit(String fileName) {
        this.justificationSet = new HashSet<>();
    }


    /**
     * Find an element given its name (also called identified).
     *
     * @param name the name you are looking for.
     * @return Empty if the element is not found, of(element) if found.
     */
    public Optional<JustificationDiagram> findByName(String name) {
        for (JustificationDiagram j : this.getJustificationSet()) {
            if (j.name().equals(name)) {
                return Optional.of(j);
            }
        }
        return Optional.empty();
    }

    /**
     * Add a new justification diagram to the set.
     *
     * @param justification the justification to add
     */
    public void add(JustificationDiagram justification) {
        // TODO check that there is no conflict (identifier duplication)
        this.justificationSet.add(justification);
    }

    /**
     * return the set of justification diagrams stored in the unit.
     *
     * @return the aforementioned set.
     */
    public Set<JustificationDiagram> getJustificationSet() {
        return justificationSet;
    }

    /**
     * Compute the set of identifiers already used at the top level of this unit.
     *
     * @return a set of string with all top-level identifiers.
     */
    public Set<String> getIdentifiers() {
        return justificationSet.stream().map(JustificationDiagram::name)
                .collect(Collectors.toSet());
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    /**
     * This method merges (applying a union approach) two units.
     *
     * @param unit the unit to be absorbed into this one.
     */
    public void merge(Unit unit) {
        // TODO check that there is no conflict (identifier duplication)
        this.justificationSet.addAll(unit.getJustificationSet());
    }
}
