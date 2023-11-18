package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;
import java.util.HashSet;
import java.util.Set;

/**
 * Model representation of a Strategy in the justification.
 */
public class Strategy extends JustificationElement implements Visitable,Cloneable{

    // supporting elements for this strategy.
    private Set<Support> supports = new HashSet<>();

    public Strategy(String identifier, String label) {
        super(identifier, label);
    }

    /**
     * Add a supporting element to this strategy.
     *
     * @param s the supporting element.
     */
    public void addSupport(Support s) {
        this.supports.add(s);
    }

    /**
     * Get the set of supporting elements.
     *
     * @return the aforementioned set.
     */
    public Set<Support> getSupports() {
        return supports;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public Strategy clone() throws CloneNotSupportedException {
        Strategy cloned_strategy = (Strategy) super.clone();
        cloned_strategy.supports = new HashSet<>();
        return cloned_strategy;
    }


}
