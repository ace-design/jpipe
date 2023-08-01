package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

public class Evidence extends Support implements Visitable {

    public Evidence(String identifier, String label) {
        super(identifier, label);
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
