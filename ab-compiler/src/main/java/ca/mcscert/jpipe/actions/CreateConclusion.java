package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.Unit;


public final class CreateConclusion implements Action {

    private final String justificationId;
    private final String identifier;
    private final String label;

    public CreateConclusion(String justificationId, String identifier, String label) {
        this.justificationId = justificationId;
        this.identifier = identifier;
        this.label = label;
    }

    @Override
    public void execute(Unit context) throws Exception {
        Conclusion conclusion = new Conclusion(identifier, label);
        context.addInto(justificationId, conclusion);
        Justification j = context.get(justificationId);
        j.setConclusion(conclusion);
    }

    @Override
    public String toString() {
        return "CreateConclusion{" +
                "container='" + justificationId + '\'' +
                ", identifier='" + identifier + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
