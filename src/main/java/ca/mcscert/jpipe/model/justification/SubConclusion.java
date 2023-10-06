package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.HashSet;
import java.util.Set;

public class SubConclusion extends Support implements Visitable {

    private Set<Strategy> supports = new HashSet<>();

    public SubConclusion(String identifier, String label) {
        super(identifier, label);
    }

    public void addSupport(Strategy strategy) {
        if (this.supports.contains(strategy)){
            this.supports.remove(strategy);
        }
        this.supports.add(strategy);
    }

    public Set<Strategy> getSupports() {
        return supports;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }
}
