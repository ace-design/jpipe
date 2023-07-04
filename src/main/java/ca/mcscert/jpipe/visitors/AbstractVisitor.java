package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.*;

public abstract class AbstractVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }


    public void visit(Unit u) {
        for(Justification j: u.getJustificationSet())
            j.accept(this);
    }

    public abstract void visit(Justification j);
    public abstract void visit(Conclusion c);
    public abstract void visit(Strategy s);
    public abstract void visit(SubConclusion sc);
    public abstract void visit(Evidence e);



}
