package ca.mcscert.jpipe.model.justification;

public abstract class JustificationElement implements Cloneable{

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

    public JustificationElement clone() throws CloneNotSupportedException{
        JustificationElement cloned_element = (JustificationElement) super.clone();
        return cloned_element;
    }

}
