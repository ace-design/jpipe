package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.Evidence;
import ca.mcscert.jpipe.model.Unit;


public final class CreateEvidence implements Action{

    private final String container;
    private final String identifier;
    private final String label;

    public CreateEvidence(String container, String identifier, String label) {
        this.container = container;
        this.identifier = identifier;
        this.label = label;
    }

    @Override
    public void execute(Unit context) throws Exception {
        Evidence e = new Evidence(this.identifier, this.label);
        context.addInto(this.container, e);
    }

    @Override
    public String toString() {
        return "CreateEvidence{" +
                "container='" + container + '\'' +
                ", identifier='" + identifier + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
