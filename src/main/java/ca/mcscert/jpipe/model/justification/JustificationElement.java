package ca.mcscert.jpipe.model.justification;

public abstract class JustificationElement {

    protected final String identifier;
    protected final String label;

    public JustificationElement(String identifier, String label) {
        this.identifier = identifier;
        this.label = label;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLabel() {
        return label;
    }
}
