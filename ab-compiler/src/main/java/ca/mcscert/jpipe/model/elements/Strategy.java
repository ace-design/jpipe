package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.util.ArrayList;
import java.util.List;

public final class Strategy extends JustificationElement {

    private final List<Support> supports;

    public Strategy(String identifier, String label) {
        super(identifier, label);
        this.supports = new ArrayList<>();
    }

    public List<Support> getSupports() {
        return supports;
    }

    @Override
    public void supports(JustificationElement that) {
        that.acceptAsSupport(this);
    }

    @Override
    protected void acceptAsSupport(Evidence e) {
        this.supports.add(e);
    }

    @Override
    protected void acceptAsSupport(SubConclusion sc) {
        this.supports.add(sc);
    }

    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }
}
