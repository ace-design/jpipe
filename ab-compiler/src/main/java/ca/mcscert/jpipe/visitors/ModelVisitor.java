package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.elements.Strategy;
import ca.mcscert.jpipe.model.elements.SubConclusion;

public abstract class ModelVisitor<T> {

    protected final T result;

    protected ModelVisitor(T result) {
        this.result = result;
    }

    public final T getResult() {
        return result;
    }

    public abstract void visit(Conclusion c);
    public abstract void visit(Evidence e);
    public abstract void visit(Strategy s);
    public abstract void visit(SubConclusion sc);

    public abstract void visit(Justification j);
    public abstract void visit(Unit u);

}
