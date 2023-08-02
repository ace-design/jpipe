package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.*;

public abstract class DefaultVisitor<T> extends AbstractVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }

    public void visit(Unit u) {
        for(Justification j: u.getJustificationSet())
            j.accept(this);
    }

    public void visit(Justification j) {
        j.getConclusion().accept(this);
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

}
