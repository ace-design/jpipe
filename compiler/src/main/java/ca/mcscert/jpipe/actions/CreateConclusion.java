package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Conclusion;


/**
 * Implement the action used to create a conclusion in a given justification.
 */
public final class CreateConclusion implements Action {

    private final String justificationId;
    private final String identifier;
    private final String label;

    /**
     * Provides necessary information to create a conclusion in a justification.
     *
     * @param justificationId the identifier of the justification used as target.
     * @param identifier the identifier to be used by this conclusion.
     * @param label the conclusion's label.
     */
    public CreateConclusion(String justificationId, String identifier, String label) {
        this.justificationId = justificationId;
        this.identifier = identifier;
        this.label = label;
    }

    @Override
    public void execute(Unit context) throws Exception {
        Conclusion conclusion = new Conclusion(identifier, label);
        context.addInto(justificationId, conclusion);
        JustificationModel j = context.get(justificationId);
        j.setConclusion(conclusion);
    }

    @Override
    public String toString() {
        return "CreateConclusion{"
                + "container='" + justificationId + '\''
                + ", identifier='" + identifier + '\''
                + ", label='" + label + '\''
                + '}';
    }
}
