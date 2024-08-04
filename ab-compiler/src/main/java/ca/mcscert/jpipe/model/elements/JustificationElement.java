package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.Visitable;

public abstract class JustificationElement implements Visitable {

    protected final String identifier;

    protected final String label;

    public JustificationElement(String identifier, String label) {
        this.identifier = identifier;
        this.label = label;
    }

    public final String getIdentifier() {
        return identifier;
    }

    public String getLabel() {
        return label;
    }

    protected void assertNotAlreadySupported(JustificationElement existing,
                                             JustificationElement candidate) {
        if (existing == null)
            return;
        String msg = String.format("%s cannot be supported by %s as it is already supported by %s",
                this.getClass().getName(), candidate, existing);
        throw new SemanticError(msg);

    }

    /* *************************************************************
     * * Double-dispatch mechanism for adding supporting relations *
     * ************************************************************* */

    public abstract void supports(JustificationElement that);

    protected void acceptAsSupport(Evidence e) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + e);
    }

    protected void acceptAsSupport(Strategy s) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + s);
    }

    protected void acceptAsSupport(Conclusion c) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + c);
    }

    protected void acceptAsSupport(SubConclusion sc) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + sc);
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "::" + this.getIdentifier();
    }

}
