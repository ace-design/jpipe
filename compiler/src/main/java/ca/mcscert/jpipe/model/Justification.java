package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.util.Collection;
import java.util.StringJoiner;

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
        StringJoiner joiner =
                new StringJoiner("\n")
                        .add("Justification " + name)
                        .add("  Conclusion: " + this.conclusion)
                        .add("  Symbol table: ")
                        .add(this.symbols.toString());
        return joiner.toString();
    }
}
