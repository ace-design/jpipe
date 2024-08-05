package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.visitors.ModelVisitor;

/**
 * Model what an Evidence is in a Justufication.
 */
public final class Evidence extends Support {

    /**
     * Instantiate an Evidence.
     *
     * @param identifier the evidence's identifier.
     * @param label its label.
     */
    public Evidence(String identifier, String label) {
        super(identifier, label);
    }

    @Override
    public void supports(JustificationElement that) {
        that.acceptAsSupport(this);
    }

    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }
}
