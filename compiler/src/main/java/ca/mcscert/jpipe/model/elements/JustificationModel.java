package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.SymbolTable;
import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.model.cloning.Replicable;
import java.util.Collection;

/**
 * Abstraction to represent patters and justification in a uniform way.
 */
public abstract class JustificationModel
        implements Visitable, Replicable<JustificationModel> {

    protected final String name;
    protected final SymbolTable<JustificationElement> symbols;
    protected Conclusion conclusion;
    protected boolean ready;

    protected JustificationModel(String name, boolean ready) {
        this.name = name;
        this.symbols = new SymbolTable<>();
        this.ready = ready;
    }

    protected JustificationModel(String name) {
        this(name, false);
    }

    public String getName() {
        return name;
    }

    public Conclusion getConclusion() {
        return conclusion;
    }

    public void publish() {
        this.ready = true;
    }

    public boolean isReady() {
        return this.ready;
    }

    /**
     * Set the one and only conclusion for this justification.
     *
     * @param conclusion the conclusion reached by this justification.
     */
    public void setConclusion(Conclusion conclusion) {
        if (this.conclusion == null) {
            this.conclusion = conclusion;
        } else {
            this.replace(this.conclusion, conclusion);
        }
    }

    /**
     * Add an element inside an unlocked justification.
     *
     * @param e the element to add
     */
    public void add(JustificationElement e) {
        this.symbols.record(e.getIdentifier(), e);
    }

    public JustificationElement get(String identifier) {
        return this.symbols.get(identifier);
    }

    public Collection<JustificationElement> contents() {
        return this.symbols.values();
    }

    /**
     * Replace an element by another one inside a justification.
     *
     * @param original the element to replace.
     * @param newOne the element replacing the original.
     */
    public void replace(JustificationElement original, JustificationElement newOne) {
        // Re-wire things in the justification model body.
        for (JustificationElement elem : this.contents()) {
            for (JustificationElement supporting : elem.getSupports()) {
                if (supporting.getIdentifier().equals(original.getIdentifier())) {
                    newOne.supports(elem);
                }
            }
        }
        // Corner case: we're replacing the conclusion
        if (this.conclusion != null && this.conclusion.equals(original)) {
            this.conclusion = (Conclusion) newOne;
        }
        // Delete the original, and add the new one.
        this.symbols.delete(original.getIdentifier());
        this.symbols.record(newOne.getIdentifier(), newOne);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name='").append(name).append('\'');
        sb.append(", symbols=").append(symbols);
        sb.append(", conclusion=").append(conclusion);
        return sb.toString();
    }

    /**
     * Method called in subclasses to implement the justification model replication (deep clone).
     * Taking an empty shell as parameter, it fills the shell with all the elements contained inside
     * this, and re-wire all relations between the newly cloned elements as they were in the
     * original object.
     *
     * @param clone the justification model to modify (side-effect) with the deep-cloning process.
     */
    protected final void deepLink(JustificationModel clone) {
        final boolean status = clone.ready; // remembering clone's status
        clone.ready = false; // opening the clone to allow internal modification

        // Building shallow clone of each element in the justification model
        for (String id : this.symbols.keys()) {
            JustificationElement elem = this.get(id).shallow();
            clone.symbols.record(id, elem); // replacing by the cloned (but still not wired) one.
        }

        // Translating the relation existing in 'this' into 'clone'.
        for (String id : clone.symbols.keys()) {
            JustificationElement cloned = clone.get(id);
            for (JustificationElement supporting : this.get(id).getSupports()) {
                JustificationElement supportingClone = clone.get(supporting.getIdentifier());
                supportingClone.supports(cloned);
            }
        }

        // Translating conclusion
        if (this.conclusion != null) {
            JustificationElement clonedConclusion = clone.get(this.conclusion.getIdentifier());
            clone.setConclusion((Conclusion) clonedConclusion); // Cast-compliant by design
        }

        clone.ready = status; // setting status back to the original one.
    }

}
