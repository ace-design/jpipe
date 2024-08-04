package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import ca.mcscert.jpipe.visitors.ModelVisitor;
import java.util.Collection;
import java.util.StringJoiner;

public final class Justification implements Visitable {

    private final String name;
    private final SymbolTable<JustificationElement> symbols;
    private Conclusion conclusion;

    public Justification(String name) {
        this.name = name;
        this.symbols = new SymbolTable<>();
    }

    public String getName() {
        return name;
    }

    public Conclusion getConclusion() {
        return conclusion;
    }

    public void setConclusion(Conclusion conclusion) {
        if (this.conclusion != null) {
            String msg = String.format("Justification %s cannot be concluded by %s as it is already using %s",
                    name, conclusion.getIdentifier(), this.conclusion.getIdentifier());
            throw new SemanticError(msg);
        }
        this.conclusion = conclusion;
    }

    public void add(JustificationElement e) {
        this.symbols.record(e.getIdentifier(), e);
    }

    public JustificationElement get(String identifier) {
        return this.symbols.get(identifier);
    }

    public Collection<JustificationElement> contents() {
        return this.symbols.values();
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
