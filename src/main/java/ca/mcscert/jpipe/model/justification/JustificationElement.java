package ca.mcscert.jpipe.model.justification;

/**
 * AST Node to represent a Justification element (conclusion, strategy, ...).
 */
public abstract class JustificationElement implements Cloneable{

    // Unique identifier of the element
    protected final String identifier;
    // Label (element textual content)
    protected final String label;

    public JustificationElement(String identifier, String label) {
        this.identifier = identifier;
        this.label = label;
    }

    /**
     * Get the unique identifier associated to the element.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the label associated to this element.
     *
     * @return the entered label (textual content of the node).
     */
    public String getLabel() {
        return label;
    }

    public JustificationElement clone() throws CloneNotSupportedException{
        JustificationElement cloned_element = (JustificationElement) super.clone();
        return cloned_element;
    }

}
