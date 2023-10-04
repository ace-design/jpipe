package ca.mcscert.jpipe.visitors;

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
 *
 * @param <T> what will this visitor produce?
 */
public interface AbstractVisitor<T> {

    void visit(Unit u);

    void visit(ConcreteJustification j);

    void visit(Conclusion c);

    void visit(Strategy s);

    void visit(SubConclusion sc);

    void visit(Evidence e);

    void visit(AbstractSupport a);

    void visit(JustificationPattern p);

    void visit(Load p);

}
