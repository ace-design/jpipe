package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

/**
 * AST model element for an abstract support (only possible in patterns).
 */
public class AbstractSupport extends Support implements Visitable {

    public AbstractSupport(String identifier, String label) {
        super(identifier, label);
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }
}
