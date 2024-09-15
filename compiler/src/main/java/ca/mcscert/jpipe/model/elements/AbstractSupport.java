package ca.mcscert.jpipe.model.elements;


import ca.mcscert.jpipe.visitors.ModelVisitor;

/**
 * Model what an abstract support is for justification patterns.
 */
public class AbstractSupport extends Support {

    public AbstractSupport(String identifier, String label) {
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

    @Override
    public String toString() {
        return "AbstractSupport::" + identifier;
    }
}
