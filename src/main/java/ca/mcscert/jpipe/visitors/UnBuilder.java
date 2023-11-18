package ca.mcscert.jpipe.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import ca.mcscert.jpipe.model.Unit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.ConcreteJustification;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.Load;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.Support;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.model.justification.AbstractSupport;


public class UnBuilder implements AbstractVisitor<JustificationDiagram>{

    private final static Logger logger = LogManager.getLogger(UnBuilder.class);

    private Map<String, JustificationElement> template_elements = new HashMap<>();

    private Map<String, List<String>> template_dependencies = new HashMap<>();

    private Conclusion template_conclusion = null;


    public void visit(JustificationPattern jp){
        logger.trace("  UnBuilding justification ["+jp.name()+"]");
        jp.conclusion().accept(this);
    }

    public void visit(ConcreteJustification cj){
        logger.trace("  UnBuilding justification ["+cj.name()+"]");
        cj.conclusion().accept(this);
    }

    public void visit(SubConclusion subc){
        logger.trace("  UnBuilding subconclusion ["+subc.getIdentifier()+"]");

        try{
            SubConclusion clone = subc.clone();
            template_elements.put(subc.getIdentifier(), clone);
            for (Strategy strategy: subc.getSupports()){
                List<String> sources = template_dependencies.getOrDefault(subc.getIdentifier(), new ArrayList<>());
                sources.add(strategy.getIdentifier());
                template_dependencies.put(subc.getIdentifier(),sources);
                strategy.accept(this);
            }
        }catch (CloneNotSupportedException e){
            throw new IllegalArgumentException(e);
        }
    }

    public void visit(Conclusion c){
        logger.trace("  UnBuilding conclusion ["+c.getIdentifier()+"]");

        try{
            template_conclusion = c.clone();
            template_dependencies.put(c.getIdentifier(), new ArrayList<String>());
            
            for (Strategy strategy: c.getSupports()){
                template_dependencies.get(c.getIdentifier()).add(strategy.getIdentifier());
                strategy.accept(this);

            }

        }catch (CloneNotSupportedException e){
            throw new IllegalArgumentException(e);
        }
    }

    public void visit(Strategy s){
        logger.trace("  UnBuilding strategy ["+s.getIdentifier()+"]");
        try{
            Strategy clone = s.clone();
            template_elements.put(s.getIdentifier(), clone);
            for (Support support : s.getSupports()){
                template_elements.put(support.getIdentifier(),support.clone());
                List<String> sources = template_dependencies.getOrDefault(s.getIdentifier(), new ArrayList<>());
                sources.add(support.getIdentifier());
                template_dependencies.put(s.getIdentifier(),sources);
                if (support instanceof SubConclusion){
                    support.accept(this);
                }
            }

        }catch (CloneNotSupportedException e){
            throw new IllegalArgumentException(e);
        }
    }

    public void visit(Unit u){
    }

    public void visit(AbstractSupport as){
    }

    public void visit(Load l){

    }

    public void visit(Evidence e) {
    }

    public Conclusion getConclusion(){
        return template_conclusion;
    }

    public Map<String, List<String>> getDependencies(){
        return template_dependencies;
    }

    public Map<String, JustificationElement> getElements(){
        return template_elements;
    }

    
    
}
