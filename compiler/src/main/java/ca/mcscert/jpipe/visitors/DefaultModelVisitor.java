package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.Strategy;
import ca.mcscert.jpipe.model.elements.SubConclusion;
import ca.mcscert.jpipe.model.elements.Support;

/**
 * Implements a default visitor for a jPipe model. Mainly provided as example.
 *
 * @param <T> the type used as a "return" from the visit.
 */
public abstract class DefaultModelVisitor<T> extends ModelVisitor<T> {


    protected DefaultModelVisitor(T result) {
        super(result);
    }

    @Override
    public void visit(Conclusion c) {
        c.getStrategy().accept(this);
    }

    @Override
    public void visit(Evidence e) {

    }

    @Override
    public void visit(Strategy s) {
        for (Support support : s.getSupports()) {
            support.accept(this);
        }
    }

    @Override
    public void visit(SubConclusion sc) {
        sc.getStrategy().accept(this);
    }

    @Override
    public void visit(Justification j) {
        j.getConclusion().accept(this);
    }

    @Override
    public void visit(Unit u) {
        for (Justification justification : u.getContents()) {
            justification.accept(this);
        }
    }

}
