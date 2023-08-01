package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

public class Probe extends ProbeOperationElement implements VisitableImp{

    public Probe(String id, String name) {
        super(id,name);
    }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
