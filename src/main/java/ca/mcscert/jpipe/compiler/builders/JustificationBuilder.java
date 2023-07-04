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

public class JustificationBuilder {


    private final String name;
    private final Map<String, JustificationElement> elements;
    private final Map<String, List<String>> dependencies;

    private Conclusion conclusion = null;

    private int line;
    private int character;

    private static Logger logger = LogManager.getLogger(JustificationBuilder.class);

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

    private void error(String message) {
        if (this.line != -1) {
            throw new CompilationError(this.line, this.character, message);
        } else {
            throw new TypeError(message);
        }
    }


    public Justification build() {
        this.line = -1; this.character = -1;
        logger.trace("Finalizing build of Justification ["+this.name+"]");
        Justification result = new Justification(this.name, this.conclusion);
        fill(this.conclusion);
        logger.trace("Finalization complete! ["+this.name+"]");
        return result;
    }

    @SuppressWarnings("all")
    private void fill(Conclusion c) {
        logger.trace("  Finalizing build of Conclusion ["+c.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(c.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            if(! (source instanceof Strategy))
                error("A Conclusion can only be supported by strategies, but ["+from+"] is not a Strategy)");
            Strategy strategy = (Strategy) source;
            c.addSupport(strategy);
            fill(strategy);
        }
    }

    @SuppressWarnings("all")
    private void fill(Strategy strategy) {
        logger.trace("  Finalizing build of Strategy ["+ strategy.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(strategy.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            if ( ! (source instanceof Support))
                error("A Strategy can only be supported by a sub-conclusion or an evidence, but [" + from +
                        "] is a " + source.getClass().getCanonicalName()+" )");
            strategy.addSupport((Support) source);
            if (source instanceof SubConclusion)
                fill((SubConclusion) source);
        }
    }

    @SuppressWarnings("all")
    private void fill(SubConclusion sc) {
        logger.trace("  Finalizing build of SubConclusion ["+ sc.getIdentifier()+"]");
        for(String from: this.dependencies.getOrDefault(sc.getIdentifier(), new ArrayList<>())) {
            JustificationElement source = this.elements.get(from);
            if(! (source instanceof Strategy))
                error("A Conclusion can only be supported by strategies, but ["+from+"] is not a Strategy)");
            Strategy strategy = (Strategy) source;
            sc.addSupport(strategy);
            fill(strategy);
        }
    }

}
