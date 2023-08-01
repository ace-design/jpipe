package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JustificationBuilder {

    protected final String name;
    protected final Map<String, JustificationElement> elements;
    protected final Map<String, List<String>> dependencies;

    protected Conclusion conclusion = null;

    protected int line;
    protected int character;
    protected static Logger logger = LogManager.getLogger(ConcreteJustificationBuilder.class);

    public JustificationBuilder(String name) {
        this.name = name;
        this.elements = new HashMap<>();
        this.dependencies = new HashMap<>();
        this.line = 0;
        this.character = 0;

    }

    public void updateLocation(int line, int character) {
        this.line = line;
        this.character = character;
    }

    public void addDependency(String from, String to) {
        if (elements.get(from) == null || elements.get(to) == null) {
            error("Cannot create relation between non existing arguments");
        }
        List<String> sources = this.dependencies.getOrDefault(to, new ArrayList<>());
        sources.add(from);
        this.dependencies.put(to, sources);
    }

    public void addElement(JustificationElement elem) {

        String identifier = elem.getIdentifier();
        if (this.elements.get(identifier) != null) {
            error("Cannot use the same element identifier twice ["+identifier+"]");
        }
        this.elements.put(identifier, elem);
    }

    public void setConclusion(Conclusion conclusion) {
        if (this.conclusion != null) {
            error("A justification cannot lead to two different conclusions");
        }
        this.conclusion = conclusion;
    }

    protected final void error(String message) {
        if (this.line != -1) {
            throw new CompilationError(this.line, this.character, message);
        } else {
            throw new TypeError(message);
        }
    }


    public abstract JustificationDiagram build();
    public abstract void checkConclusionPredecessor(JustificationElement e);
    public abstract void checkStrategyPredecessor(JustificationElement e);
    public abstract void checkSubConclusionPredecessor(JustificationElement e);


    @SuppressWarnings("all")
    protected final void fill(Conclusion c) {
        logger.trace("  Finalizing build of Conclusion ["+c.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(c.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            checkConclusionPredecessor(source);
            Strategy strategy = (Strategy) source;
            c.addSupport(strategy);
            fill(strategy);
        }
    }

    @SuppressWarnings("all")
    protected final void fill(Strategy strategy) {
        logger.trace("  Finalizing build of Strategy ["+ strategy.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(strategy.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            checkStrategyPredecessor(source);
            strategy.addSupport((Support) source);
            if (source instanceof SubConclusion)
                fill((SubConclusion) source);
        }
    }

    @SuppressWarnings("all")
    protected final void fill(SubConclusion sc) {
        logger.trace("  Finalizing build of SubConclusion ["+ sc.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(sc.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            checkSubConclusionPredecessor(source);
            Strategy strategy = (Strategy) source;
            sc.addSupport(strategy);
            fill(strategy);
        }
    }



}
