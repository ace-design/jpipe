package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.*;
import ca.mcscert.jpipe.model.justification.*;

public abstract class AbstractVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }


    public void visit(Unit u) {
        for(JustificationDiagram j: u.getJustificationSet())
            j.accept(this);
    }

    public abstract void visit(ConcreteJustification j);
    public abstract void visit(Conclusion c);
    public abstract void visit(Strategy s);
    public abstract void visit(SubConclusion sc);
    public abstract void visit(Evidence e);
    public abstract void visit(AbstractSupport a);
    public abstract void visit(JustificationPattern p);

}
