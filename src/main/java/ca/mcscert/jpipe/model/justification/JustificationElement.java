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

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Support){
            Support foreign_obj=(Support)obj;
            if (foreign_obj.getIdentifier().equals(this.identifier)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }
}
