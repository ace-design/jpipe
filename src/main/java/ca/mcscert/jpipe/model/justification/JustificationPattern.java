package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.Map;
import java.util.List;

public record JustificationPattern(String name, Conclusion conclusion, Map<String, JustificationElement> elements, Map<String,List<String>> dependencies) implements JustificationDiagram {

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    public Map<String, JustificationElement> elements(){
        return elements;
    }

    public Map<String, List<String>> dependencies(){
        return dependencies;
    }

}
