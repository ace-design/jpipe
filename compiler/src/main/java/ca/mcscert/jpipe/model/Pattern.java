package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.ModelVisitor;

/**
 * Define what a justification pattern is.
 */
public final class Pattern extends JustificationModel {


    /**
     * Creates a justification based on its name.
     *
     * @param name the name (identifier) of the pattern
     */
    public Pattern(String name) {
        super(name);
    }

    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pattern{");
        sb.append("name='").append(name).append('\'');
        sb.append(", symbols=").append(symbols);
        sb.append(", conclusion=").append(conclusion);
        sb.append('}');
        return sb.toString();
    }

}
