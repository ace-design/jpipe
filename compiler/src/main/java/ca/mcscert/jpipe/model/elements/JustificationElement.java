package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.model.cloning.ShallowCloneable;
import java.util.Objects;
import java.util.Set;

/**
 * Define what a justification element is. A Justification element is "anything" that can be used
 * to instantiate a "supports" relation inside a justification.
 */
public abstract class JustificationElement
        implements Visitable, ShallowCloneable<JustificationElement> {

    protected final String identifier;
    protected final String label;

    /**
     * A justification element comes minimally with a label and an identifier.
     *
     * @param identifier the unique (inside the justification) identifier to be used.
     * @param label the label associated to the element.
     */
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

    /**
     * Throws a SemanticError if there is an existing element, and if candidate and existing
     * do not share the same identifier.
     *
     * @param existing the element one is trying to replace.
     * @param candidate the element to be used to replace existing.
     */
    protected void assertCanBeReplaced(JustificationElement existing,
                                       JustificationElement candidate) {
        if (existing == null || existing.getIdentifier().equals(candidate.getIdentifier())) {
            return;
        }
        String msg = String.format("%s cannot be supported by %s as it is already supported by %s",
                this.getClass().getName(), candidate, existing);
        throw new SemanticError(msg);

    }

    /**
     * Identify the justification elements supporting this one.
     *
     * @return a Set of domain elements acting as support for this one.
     */
    public abstract Set<JustificationElement> getSupports();


    /* *************************************************************
     * * Double-dispatch mechanism for adding supporting relations *
     * ************************************************************* */

    /**
     * Double-dispatch mechanism to model which element can support what as part of a justification.
     * A justification elements must implement this method, as a way to double dispatch to
     * acceptAsSupport().
     *
     * @param that the elements one wants to support (as in "this supports that" in DSL syntax)
     */
    public abstract void supports(JustificationElement that);


    /**
     * Re-implement this method when extending JustificationElement if your element can be
     * supported by an Evidence. Default behavior is to reject support.
     *
     * @param e the evidence to be used as a support of this. (as in "e supports this")
     */
    protected void acceptAsSupport(Evidence e) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + e);
    }

    /**
     * Re-implement this method when extending JustificationElement if your element can be
     * supported by a Strategy. Default behavior is to reject support.
     *
     * @param s the strategy to be used as a support of this. (as in "s supports this")
     */
    protected void acceptAsSupport(Strategy s) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + s);
    }

    /**
     * Re-implement this method when extending JustificationElement if your element can be
     * supported by a Conclusion. Default behavior is to reject support.
     *
     * <p>Allowing a conclusion to support something is weird, so it's flagged as final.</p>
     *
     * @param c the conclusion to be used as a support of this. (as in "c supports this")
     */
    protected final void acceptAsSupport(Conclusion c) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + c);
    }

    /**
     * Re-implement this method when extending JustificationElement if your element can be
     * supported by an SubConclusion. Default behavior is to reject support.
     *
     * @param sc the sub-conclusion to be used as a support of this. (as in "sc supports this")
     */
    protected void acceptAsSupport(SubConclusion sc) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + sc);
    }

    protected void acceptAsSupport(AbstractSupport as) {
        throw new IllegalArgumentException("Justification element "
                + this + " cannot be supported by " + as);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JustificationElement that = (JustificationElement) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "::" + this.getIdentifier();
    }

}
