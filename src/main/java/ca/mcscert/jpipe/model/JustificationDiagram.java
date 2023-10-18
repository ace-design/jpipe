package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.model.justification.Support;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


public interface JustificationDiagram extends Visitable {

    /**
     * Get the name of the Justification Diagram
     * @return the unique name, as a string
     */
    String name();

    /**
     * Give access to the entry point (the conclusion) of the diagram
     * @return the diagram's conclusion
     */
    Conclusion conclusion();

    public static Map<String, JustificationElement> template_elements = new HashMap<>();

    public static Map<String, List<String>> template_dependencies = new HashMap<>();


    private static void traverseGraph(SubConclusion subconclusion) throws CloneNotSupportedException{
        for (Strategy strategy : subconclusion.getSupports()){
            try{
                template_elements.put(strategy.getIdentifier(), strategy.clone());
            }catch (CloneNotSupportedException e){
                throw new RuntimeException(e);
            }
            List<String> sources = template_dependencies.getOrDefault(subconclusion.getIdentifier(), new ArrayList<>());
            sources.add(strategy.getIdentifier());
            template_dependencies.put(subconclusion.getIdentifier(), sources);
            traverseGraph(strategy);
        }
    }

    private static void traverseGraph(Strategy strategy) throws CloneNotSupportedException{
        for (Support support : strategy.getSupports()){
            try{
                template_elements.put(support.getIdentifier(), support.clone());
            }catch (CloneNotSupportedException e){
                throw new RuntimeException(e);
            }
            List<String> sources = template_dependencies.getOrDefault(strategy.getIdentifier(), new ArrayList<>());
            sources.add(support.getIdentifier());
            template_dependencies.put(strategy.getIdentifier(),sources);

            if (support instanceof SubConclusion){
                traverseGraph((SubConclusion)support);
            }
        }
    }

    public static Conclusion traverseGraph(Conclusion conclusion) throws CloneNotSupportedException{
        Conclusion template_conclusion = conclusion.clone();
        template_dependencies.put(conclusion.getIdentifier(),new ArrayList<String>());
        for (Strategy strategy : conclusion.getSupports()){
            try{
                template_elements.put(strategy.getIdentifier(), strategy.clone());
            }catch (CloneNotSupportedException e){
                throw new RuntimeException(e);
            }
            template_dependencies.get(conclusion.getIdentifier()).add(strategy.getIdentifier());
            traverseGraph(strategy);
        }
        return template_conclusion;
    }
    


}
