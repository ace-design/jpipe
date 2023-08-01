package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractVisitor;

public class ConcreteJustification implements JustificationDiagram {

    private final Conclusion conclusion;
    private final String name;

    public ConcreteJustification(String name, Conclusion conclusion) {
        this.conclusion = conclusion;
        this.name = name;
    }

    public Conclusion getConclusion() {
        return conclusion;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

}
