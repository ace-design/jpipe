package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

public class Expectation extends ExpectationElement implements VisitableImp {

    public Expectation(String symbol, String op, int val) {
        super(symbol, op, val);
    }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
