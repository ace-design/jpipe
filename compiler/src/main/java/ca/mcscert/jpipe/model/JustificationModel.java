package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.elements.Conclusion;
import ca.mcscert.jpipe.model.elements.JustificationElement;
import java.util.Collection;

/**
 * Abstraction to represent patters and justification in a uniform way.
 */
public abstract class JustificationModel implements Visitable {

    protected final String name;
    protected final SymbolTable<JustificationElement> symbols;
    protected Conclusion conclusion;
    protected final Pattern parent;

    protected JustificationModel(String name) {
        this.name = name;
        this.symbols = new SymbolTable<>();
        this.parent = null;
    }

    protected JustificationModel(String name, Pattern parent) {
        this.name = name;
        this.symbols = new SymbolTable<>(parent.symbols);
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Conclusion getConclusion() {
        return conclusion;
    }

    /**
     * Set the one and only conclusion for this justification (cannot be changed).
     *
     * @param conclusion the conclusion reached by this justification.
     */
    public void setConclusion(Conclusion conclusion) {
        if (this.conclusion != null) {
            String msg = String.format("Justification %s cannot be concluded by %s as it is "
                            + "already using %s",
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
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name='").append(name).append('\'');
        sb.append(", symbols=").append(symbols);
        sb.append(", conclusion=").append(conclusion);
        sb.append(", parent=").append(parent);
        return sb.toString();
    }
}
