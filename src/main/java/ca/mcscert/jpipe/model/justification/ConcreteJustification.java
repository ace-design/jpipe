package ca.mcscert.jpipe.model.justification;

import java.util.List;
import java.util.Map;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

public record ConcreteJustification(String name, Conclusion conclusion) implements JustificationDiagram {


    public static Conclusion template_conclusion;

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }


    public void unbuild(){
        try{
            unbuilder.traverseGraph(conclusion);
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }
    

    public Map<String, JustificationElement> templateElements(){
        return unbuilder.templateElements();
    }

    public Map<String, List<String>> templateDependencies(){
        return unbuilder.templateDependencies();
    }

    public Conclusion templateConclusion(){
        return unbuilder.templateConclusion();
    }

}
