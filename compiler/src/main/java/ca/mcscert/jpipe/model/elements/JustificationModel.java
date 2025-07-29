package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.error.UnknownSymbol;
import ca.mcscert.jpipe.model.RepTable;
import ca.mcscert.jpipe.model.SymbolTable;
import ca.mcscert.jpipe.model.SymbolTree;
import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.model.cloning.Replicable;
import java.util.Collection;
import java.util.Set;

/**
 * Abstraction to represent patterns and justification in a uniform way.
 */
public abstract class JustificationModel
        implements Visitable, Replicable<JustificationModel> {

    protected final String name;
    protected final SymbolTree symbolTree;
    protected RepTable<JustificationElement> repTable;
    protected boolean frozen;

    protected JustificationModel(String name, boolean isFrozen) {
        this.name = name;
        this.symbolTree = new SymbolTree();
        this.repTable = new RepTable<>();
        this.frozen = isFrozen;
    }

    protected JustificationModel(String name) {
        this(name, false);
    }

    public String getName() {
        return name;
    }

    public void freeze() {
        this.frozen = true;
    }

    public void unfreeze() {
        this.frozen = false;
    }

    public boolean isFrozen() {
        return this.frozen;
    }


    /**
     * Get an element inside an unlocked justification.
     *
     * @param identifier String identifier
     */
    public JustificationElement get(String identifier) {
        if (identifier.contains(":")) {
            String[] parts = identifier.split(":");
            String id = parts[parts.length - 1];
            return this.symbolTree.get(id, identifier);
        }
        return this.symbolTree.get(identifier);
    }

    public Collection<JustificationElement> contents() {
        return this.symbolTree.values();
    }

    public RepTable<JustificationElement> representations() {
        return this.repTable;
    }

    /**
     * Add an element inside an unlocked justification.
     *
     * @param e the element to add
     */
    public void add(JustificationElement e) {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot add an element to a frozen justification");
        }
        e.setContainer(this);
        e.setScope(e.fullyQualifiedName());
        this.symbolTree.record(e.getIdentifier(), e);
        this.repTable.record(e);

    }

    /**
     * Add an element inside an unlocked justification with the representative element.
     *
     * @param e the element to add
     * @param rep the representative element of e
     */
    public void add(JustificationElement e, JustificationElement rep) {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot add an element to a frozen justification");
        }
        this.symbolTree.record(e.getIdentifier(), e);
        e.setContainer(this);
        e.recordScope(this.getName(), rep.getScope());
        this.repTable.record(e, rep);
    }

    /**
     * Add an element inside an unlocked justification with the representative elements.
     *
     * @param e the element to add
     * @param reps the representative element of e
     */
    public void add(JustificationElement e, Set<JustificationElement> reps) {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot add an element to a frozen justification");
        }
        this.symbolTree.record(e.getIdentifier(), e);
        e.setContainer(this);
        for (JustificationElement rep : reps) {
            if (!e.getIdentifier().equals(rep.getIdentifier())) {
                e.recordScope(this.getName());
            } else {
                e.recordScope(this.getName(), rep.getScope());
            }
            this.repTable.record(e, rep);
        }

    }

    /**
     * Remove an element from a given justification.
     *
     * @param e the element to remove.
     */
    public void remove(JustificationElement e) {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot remove an element from a frozen justification");
        }
        if (! this.symbolTree.exists(e.getIdentifier())) {
            String msg = "Cannot remove a non-existing element from a justification";
            throw new IllegalStateException(msg);
        }
        this.symbolTree.delete(e.getIdentifier(), e);
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
        // Delete the original, and add the new one.
        this.add(newOne, original);
        this.remove(original);

    }

    /**
     * Method called in subclasses to implement the justification model replication (deep clone).
     * Taking an empty shell as parameter, it fills the shell with all the elements contained inside
     * this, and re-wire all relations between the newly cloned elements as they were in the
     * original object.
     *
     * @param clone the justification model to modify (side effect) with the deep-cloning process.
     */
    protected final void deepLink(JustificationModel clone) {
        clone.unfreeze(); // opening the clone for modification

        // Building shallow clone of each element in the justification model
        for (String id : this.symbolTree.keys()) {
            JustificationElement elem = this.get(id).shallow();
            clone.add(elem, this.get(id)); // replacing by the cloned (but still not wired) one.
        }

        // Translating the relation existing in 'this' into 'clone'.
        for (String id : clone.symbolTree.keys()) {
            JustificationElement cloned = clone.get(id);
            for (JustificationElement supporting : this.get(id).getSupports()) {
                JustificationElement supportingClone = clone.get(supporting.getIdentifier());
                supportingClone.supports(cloned);
            }
        }
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JustificationModel{");
        sb.append("name='").append(name).append('\'');
        sb.append(", symbols=").append(symbolTree);
        sb.append(", ready=").append(frozen);
        sb.append('}');
        return sb.toString();
    }

    public JustificationElement getDirectReplacementOf(JustificationElement elem) {
        return repTable.getParent(elem);
    }

    public JustificationElement getOriginalOf(JustificationElement elem) {
        return repTable.getOriginalParent(elem);
    }

    public boolean hasReplacementRecord(JustificationElement elem) {
        return repTable.containsKey(elem);
    }

}
