package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;
import java.util.HashSet;
import java.util.Set;

/**
 * Represent the SubConclusion AST node in the model.
 */
public class SubConclusion extends Support implements Visitable, Cloneable {

    // supporting strategies leading to that sub conclusion.
    private Set<Strategy> supports = new HashSet<>();

    public SubConclusion(String identifier, String label) {
        super(identifier, label);
    }

    /**
     * add a supporting strategy that helps in reaching this sub-conclusion.
     *
     * @param strategy the instance of strategy supporting the sub conclusion
     */
    public void addSupport(Strategy strategy) {
        this.supports.add(strategy);
    }

    /**
     * Get the set of strategies that support the sub-conclusion.
     *
     * @return the aforementioned set
     */
    public Set<Strategy> getSupports() {
        return supports;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public SubConclusion clone() throws CloneNotSupportedException {
        SubConclusion cloned_subconclusion = (SubConclusion) super.clone();
        cloned_subconclusion.supports = new HashSet<>();
        return cloned_subconclusion;
    }
}
