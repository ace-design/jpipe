package ca.mcscert.jpipe.compiler.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.model.justification.Support;

public class JustificationUnBuilder {


    private Map<String, JustificationElement> template_elements = new HashMap<>();

    private Map<String, List<String>> template_dependencies = new HashMap<>();

    private Conclusion template_conclusion = null;



    private void traverseGraph(SubConclusion subconclusion) throws CloneNotSupportedException{
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

    private void traverseGraph(Strategy strategy) throws CloneNotSupportedException{
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

    public void traverseGraph(Conclusion conclusion) throws CloneNotSupportedException{
        template_conclusion = conclusion.clone();
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
    }

    public Conclusion templateConclusion(){
        return template_conclusion;
    }

    public Map<String, JustificationElement>  templateElements(){
        return template_elements;
    }

    public Map<String, List<String>>  templateDependencies(){
        return template_dependencies;
    }
    
}
