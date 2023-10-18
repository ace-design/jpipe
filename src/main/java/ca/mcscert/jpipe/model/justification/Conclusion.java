package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.HashSet;
import java.util.Set;

public class Conclusion extends JustificationElement implements Visitable, Cloneable {

    private Set<Strategy> supports = new HashSet<>();

    public Conclusion(String identifier, String label) {
        super(identifier, label);
    }

    public void addSupport(Strategy from) {
        this.supports.add(from);
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    public Set<Strategy> getSupports() {
        return supports;
    }

    @Override
    public Conclusion clone() throws CloneNotSupportedException {
        Conclusion cloned_conclusion = (Conclusion) super.clone();
        cloned_conclusion.supports = new HashSet<>();
        return cloned_conclusion;
    }
}
