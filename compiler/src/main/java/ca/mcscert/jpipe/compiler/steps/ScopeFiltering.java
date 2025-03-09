package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.JustificationModel;

/**
 * Filter a unit to extract one given justification element inside this unit.
 */
public class ScopeFiltering extends Transformation<Unit, JustificationModel> {

    private final String identifier;

    /**
     * We provide as input to the step the justification we are looking for.
     *
     * @param identifier the name of the justification we want to keep.
     */
    public ScopeFiltering(String identifier) {
        this.identifier = identifier;
    }

    @Override
    protected JustificationModel run(Unit input, String source) throws Exception {
        return input.get(this.identifier);
    }

}
