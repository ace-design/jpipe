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

public abstract class DefaultVisitor<T> extends AbstractVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }

    public void visit(Unit u) {
        for(JustificationDiagram j: u.getJustificationSet())
            j.accept(this);
    }

    public void visit(ConcreteJustification j) {
        j.conclusion().accept(this);
    }

    public void visit(JustificationPattern p) {
        p.conclusion().accept(this);
    }

    public void visit(Conclusion c) {
        for(Strategy s: c.getSupports()) {
            s.accept(this);
        }
    }
    public void visit(Strategy s) {
        for(Support sup: s.getSupports()) {
            sup.accept(this);
        }
    }
    public void visit(SubConclusion sc) {
        for(Strategy s: sc.getSupports()) {
            s.accept(this);
        }
    }
    public void visit(Evidence e) {

    }

    public void visit(AbstractSupport a) {

    }

    public void visit(Load l){
        
    }

}
