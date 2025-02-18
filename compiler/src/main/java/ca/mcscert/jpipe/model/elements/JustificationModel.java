package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.SymbolTable;
import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.model.cloning.Replicable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Abstraction to represent patters and justification in a uniform way.
 */
public abstract class JustificationModel
        implements Visitable, Replicable<JustificationModel> {

    protected final String name;
    protected final SymbolTable<JustificationElement> symbols;
    protected Conclusion conclusion;

    private boolean locked = false;

    protected JustificationModel(String name) {
        this.name = name;
        this.symbols = new SymbolTable<>();
    }

    public String getName() {
        return name;
    }

    public Conclusion getConclusion() {
        return conclusion;
    }

    public void lock() {
        this.locked = true;
    }

    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Set the one and only conclusion for this justification (cannot be changed).
     *
     * @param conclusion the conclusion reached by this justification.
     */
    public void setConclusion(Conclusion conclusion) {
        if (locked) {
            throw new IllegalArgumentException("Cannot set conclusion on a locked model");
        }
        // Can add a conclusion if no conclusion, or replacing the one we already have by a
        // new object.
        if (this.conclusion != null
                && ! this.conclusion.getIdentifier().equals(conclusion.getIdentifier())) {
            String msg = String.format("Justification %s cannot be concluded by %s as it is "
                            + "already using %s",
                    name, conclusion.getIdentifier(), this.conclusion.getIdentifier());
            throw new SemanticError(msg);
        }
        this.conclusion = conclusion;
    }

    /**
     * Add an element inside an unlocked justification.
     *
     * @param e the element to add
     */
    public void add(JustificationElement e) {
        if (locked) {
            throw new IllegalArgumentException("Cannot add an element to a locked model");
        }
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
        if (this.conclusion != null
                && this.conclusion.getIdentifier().equals(original.getIdentifier())) {
            this.setConclusion((Conclusion) newOne);
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

    protected final void deepLink(JustificationModel clone) {
        clone.locked = false;
        // Building shallow clone of eac element in the justification model
        for (String id : this.symbols.keys()) {
            JustificationElement elem = this.get(id).shallow();
            clone.symbols.record(id, elem); // replacing by the cloned (but still not wired) one.
        }

        // Translating the relation existing in this into the clone.
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
    }

}
