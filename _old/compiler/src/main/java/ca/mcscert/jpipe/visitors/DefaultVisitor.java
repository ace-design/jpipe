package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.justification.AbstractSupport;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.ConcreteJustification;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Load;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.model.justification.Support;

/**
 * Default visitor implementation.
 * this class gives the mechanical traversal of each element (propagating to others when needed).
 *
 * @param <T> the type of output this visitor is
 */
public abstract class DefaultVisitor<T> implements AbstractVisitor<T> {

    // The result produced by the visitor
    protected T result;

    /**
     * Get access to the result at the end of the visit.
     *
     * @return the instance of T obtained at the end of the visit.
     */
    public final T getResult() {
        return result;
    }

    /**
     * Visit a unit element.
     *
     * @param u the element to visit.
     */
    public void visit(Unit u) {
        for (JustificationDiagram j : u.getJustificationSet()) {
            j.accept(this);
        }
    }

    /**
     * Visit a concrete justification element.
     *
     * @param j the element to visit.
     */
    public void visit(ConcreteJustification j) {
        j.conclusion().accept(this);
    }

    /**
     * Visit a justification pattern element.
     *
     * @param p the element to visit.
     */
    public void visit(JustificationPattern p) {
        p.conclusion().accept(this);
    }

    /**
     * Visit a conclusion element.
     *
     * @param c the element to visit.
     */
    public void visit(Conclusion c) {
        for (Strategy s : c.getSupports()) {
            s.accept(this);
        }
    }

    /**
     * Visit a strategy element.
     *
     * @param s the element to visit.
     */
    public void visit(Strategy s) {
        for (Support sup : s.getSupports()) {
            sup.accept(this);
        }
    }

    /**
     * Visit a sub conclusion element.
     *
     * @param sc the element to visit.
     */
    public void visit(SubConclusion sc) {
        for (Strategy s : sc.getSupports()) {
            s.accept(this);
        }
    }

    public void visit(Evidence e) { }

    public void visit(AbstractSupport a) { }

    public void visit(Load l) { }

}
