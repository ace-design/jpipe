package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.visitors.ModelVisitor;

public final class Conclusion extends JustificationElement {

    private Strategy strategy;

    public Conclusion(String identifier, String label) {
        super(identifier, label);
    }


    public Strategy getStrategy() {
        return strategy;
    }

    @Override
    public void supports(JustificationElement that) {
        that.acceptAsSupport(this);
    }

    @Override
    protected void acceptAsSupport(Strategy s) {
        assertNotAlreadySupported(this.strategy, s);
        this.strategy = s;
    }

    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }
}
