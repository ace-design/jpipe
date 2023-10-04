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

/**
 * Interface for visiting an AST model. Basic Visitor pattern implementation.
 * TODO now that we have a DefaultVisitor, this should be refactored into an interface
 *
 * @param <T> what will this visitor produce?
 */
public abstract class AbstractVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }


    /**
     * Default implementation for visiting an unit.
     *
     * @param u the unit to visit.
     */
    public void visit(Unit u) {
        // TODO not sure this should be part of the implementation...
        for (JustificationDiagram j : u.getJustificationSet()) {
            j.accept(this);
        }
    }

    public abstract void visit(ConcreteJustification j);


    public abstract void visit(Conclusion c);

    public abstract void visit(Strategy s);

    public abstract void visit(SubConclusion sc);

    public abstract void visit(Evidence e);

    public abstract void visit(AbstractSupport a);

    public abstract void visit(JustificationPattern p);

    public abstract void visit(Load p);

}
