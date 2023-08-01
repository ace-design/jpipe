package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

public interface VisitableImp {
    void accept(AbstractImpVisitor<?> visitor);
}
