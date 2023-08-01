package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.*;

public abstract class AbstractImpVisitor<T> {

    protected T result;

    public T getResult() {
        return result;
    }

    public void visit(Unit_Imp unitImp) {
        for(Implementation implementation: unitImp.getImplementationSet())
            implementation.accept(this);
    }

    public abstract void visit(Probe p);

    public void visit(Implementation imp) {
        for (Implements anImplements: imp.getImplementsSet())
            anImplements.accept(this);
    }

    public abstract void visit(Implements i);
    public abstract void visit(Expectation expectation);
    public abstract void visit(Operation operation);
    public abstract void visit(Load load);
}
