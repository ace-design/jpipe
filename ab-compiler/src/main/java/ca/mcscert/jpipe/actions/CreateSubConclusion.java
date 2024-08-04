package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.SubConclusion;
import ca.mcscert.jpipe.model.Unit;


public final class CreateSubConclusion implements Action{

    private final String container;
    private final String identifier;
    private final String label;

    public CreateSubConclusion(String container, String identifier, String label) {
        this.container = container;
        this.identifier = identifier;
        this.label = label;
    }

    @Override
    public void execute(Unit context) throws Exception {
        SubConclusion sc = new SubConclusion(identifier, label);
        context.addInto(container, sc);

    }

    @Override
    public String toString() {
        return "CreateSubConclusion{" +
                "container='" + container + '\'' +
                ", identifier='" + identifier + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
