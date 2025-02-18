package ca.mcscert.jpipe.model.elements;

import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.io.Serializable;

/**
 * Define what a Justification is in a jPipe model.
 */
public final class Justification extends JustificationModel {

    /**
     * Creates a justification based on its name.
     *
     * @param name the name (identifier) to be used.
     */
    public Justification(String name) {
        super(name);
    }

    @Override
    public void accept(ModelVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Justification{");
        sb.append(super.toString());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public JustificationModel replicate() {
        Justification clone = new Justification(this.name);
        deepLink(clone);
        return clone;
    }
}
