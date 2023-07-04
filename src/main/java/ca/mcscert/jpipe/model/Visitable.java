package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

public interface Visitable {

    void accept(AbstractVisitor<?> visitor);

}
