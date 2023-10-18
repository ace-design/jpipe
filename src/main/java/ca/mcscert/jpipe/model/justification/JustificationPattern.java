package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

import java.util.Map;
import java.util.List;



public record JustificationPattern(String name, Conclusion conclusion) implements JustificationDiagram {

    public static Conclusion template_conclusion;

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }


    public void unbuild(){
        try{
            template_conclusion=JustificationDiagram.traverseGraph(conclusion);
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }
    

    public Map<String, JustificationElement> template_elements(){
        return template_elements;
    }

    public Map<String, List<String>> template_dependencies(){
        return template_dependencies;
    }

    public Conclusion template_conclusion(){
        return template_conclusion;
    }

}
