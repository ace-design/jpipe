package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.HashSet;
import java.util.Set;



public class Strategy extends JustificationElement implements Visitable {

    private Set<Support> supports = new HashSet<>();

    public Strategy(String identifier, String label) {
        super(identifier, label);
    }

    public void addSupport(Support s) {
        if (supports.contains(s)){
            supports.remove(s);
        }
        this.supports.add(s);
    }

    public Set<Support> getSupports() {
        return supports;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }


}
