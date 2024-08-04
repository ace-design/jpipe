package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.ModelVisitor;

public interface Visitable {

    void accept(ModelVisitor<?> visitor);

}
