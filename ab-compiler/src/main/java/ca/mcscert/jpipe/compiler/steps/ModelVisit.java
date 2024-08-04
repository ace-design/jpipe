package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.ModelVisitor;

public final class ModelVisit<I extends Visitable,O> extends Transformation<I,O> {

    private final ModelVisitor<O> visitor;

    public ModelVisit(ModelVisitor<O> visitor) {
        this.visitor = visitor;
    }

    @Override
    protected O run(I input, String source) throws Exception {
        input.accept(visitor);
        return visitor.getResult();
    }

}
