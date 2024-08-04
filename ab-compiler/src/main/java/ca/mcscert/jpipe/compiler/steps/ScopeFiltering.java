package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;

public class ScopeFiltering extends Transformation<Unit, Justification> {

    private final String identifier;

    public ScopeFiltering(String identifier) {
        this.identifier = identifier;
    }

    @Override
    protected Justification run(Unit input, String source) throws Exception {
        return input.get(this.identifier);
    }

}
