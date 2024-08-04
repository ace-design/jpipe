package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Justification;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.model.Unit;


public final class CreateRelation implements Action {

    private final String container;
    private final String from;
    private final String to;

    public CreateRelation(String container, String from, String to) {
        this.container = container;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(Unit context) throws Exception {
        Justification justification = context.get(container);
        JustificationElement f = justification.get(from);
        JustificationElement t = justification.get(to);
        f.supports(t);
    }

    @Override
    public String toString() {
        return "CreateRelation{" +
                "container='" + container + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
