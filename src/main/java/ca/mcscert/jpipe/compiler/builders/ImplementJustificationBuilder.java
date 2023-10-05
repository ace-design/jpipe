package ca.mcscert.jpipe.compiler.builders;

import java.util.ArrayList;
import java.util.List;

import ca.mcscert.jpipe.model.*;
import ca.mcscert.jpipe.model.justification.*;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;


public class ImplementJustificationBuilder extends ConcreteJustificationBuilder{

    private final JustificationPattern template;

    public ImplementJustificationBuilder(String name, JustificationPattern template){
        super(name);
        this.template=template;
        extract();
    }

    private void extract(){
        this.elements=template.elements();
        this.dependencies=template.dependencies();
        this.conclusion=template.conclusion();
    }

    @Override
    public void addElement(JustificationElement elem) {
        String identifier = elem.getIdentifier();
        this.elements.put(identifier,elem);
    }

    @Override
    public void addDependency(String from, String to) {
        List<String> sources = this.dependencies.getOrDefault(to, new ArrayList<>());
        sources.add(from);
        this.dependencies.put(to, sources);
    }

    @Override
    public void setConclusion(Conclusion conclusion) {
        this.conclusion = conclusion;
    }





    
}
