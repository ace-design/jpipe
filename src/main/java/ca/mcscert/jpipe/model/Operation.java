package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

public class Operation extends ProbeOperationElement implements VisitableImp{

    public Operation(String id, String value) {
        super(id, value);
    }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
