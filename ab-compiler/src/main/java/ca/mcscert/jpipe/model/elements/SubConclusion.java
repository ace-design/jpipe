package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.visitors.ModelVisitor;

/**
 * Define what a sub-conclusion is inside a jPipe model.
 */
public final class SubConclusion extends Support {

    private Strategy strategy;

    /**
     * Instantiates a sub-conclusion with an identifier and a label.
     *
     * @param identifier the sub-conclusion's identifier (unique inside the justification).
     * @param label the sub-conclusion's label (unique inside the justification).
     */
    public SubConclusion(String identifier, String label) {
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
