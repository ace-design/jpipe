package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

/**
 * Visitable interface (Visitor Pattern).
 * Elements implementing this interface accept a visitor.
 */
public interface Visitable {

    void accept(AbstractVisitor<?> visitor);

}
