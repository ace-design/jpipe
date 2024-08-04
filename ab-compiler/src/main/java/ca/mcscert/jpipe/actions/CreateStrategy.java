package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.elements.Strategy;
import ca.mcscert.jpipe.model.Unit;


public final class CreateStrategy implements Action{

    private final String container;
    private final String identifier;
    private final String label;

    public CreateStrategy(String container, String identifier, String label) {
        this.container = container;
        this.identifier = identifier;
        this.label = label;
    }

    @Override
    public void execute(Unit context) throws Exception {
        context.addInto(container, new Strategy(identifier, label));
    }

    @Override
    public String toString() {
        return "CreateStrategy{" +
                "container='" + container + '\'' +
                ", identifier='" + identifier + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

}
