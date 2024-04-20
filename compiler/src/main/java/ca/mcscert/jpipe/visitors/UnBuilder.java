package ca.mcscert.jpipe.visitors;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.justification.AbstractSupport;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.ConcreteJustification;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Load;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.model.justification.Support;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of a default visitor to unbuild the given diagram.
 */
public class UnBuilder implements AbstractVisitor<JustificationDiagram> {

  private final Logger logger = LogManager.getLogger(UnBuilder.class);

  private Map<String, JustificationElement> templateElements = new HashMap<>();

  private Map<String, List<String>> templateDependencies = new HashMap<>();

  private Conclusion templateConclusion = null;


  public void visit(JustificationPattern jp) {
    logger.trace("  UnBuilding justification [" + jp.name() + "]");
    jp.conclusion().accept(this);
  }

  public void visit(ConcreteJustification cj) {
    logger.trace("  UnBuilding justification [" + cj.name() + "]");
    cj.conclusion().accept(this);
  }

  /**
   * Visiting a SubConclusion object. Clone the object, add it to list of elements.
   * Visit all it's Strategy dependencies.
   *
   * @param subc SubConclusion object to unbuild.
   */
  public void visit(SubConclusion subc) {
    logger.trace("  UnBuilding subconclusion [" + subc.getIdentifier() + "]");

    try {
      SubConclusion clone = subc.clone();
      templateElements.put(subc.getIdentifier(), clone);
      for (Strategy strategy : subc.getSupports()) {
        List<String> sources =
            templateDependencies.getOrDefault(subc.getIdentifier(), new ArrayList<>());
        sources.add(strategy.getIdentifier());
        templateDependencies.put(subc.getIdentifier(), sources);
        strategy.accept(this);
      }
    } catch (CloneNotSupportedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Visiting the Conclusion object. Clone the object, add it to list of elements.
   * Visit all it's Strategy dependencies.
   *
   * @param c Conclusion to unbuild.
   */
  public void visit(Conclusion c) {
    logger.trace("  UnBuilding conclusion [" + c.getIdentifier() + "]");

    try {
      templateConclusion = c.clone();
      templateDependencies.put(c.getIdentifier(), new ArrayList<String>());

      for (Strategy strategy : c.getSupports()) {
        templateDependencies.get(c.getIdentifier()).add(strategy.getIdentifier());
        strategy.accept(this);

      }

    } catch (CloneNotSupportedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Visiting a Strategy object. Clone the object, add it to list of elements.
   * Visit all Support dependencies.
   *
   * @param s Strategy object to unbuild.
   */
  public void visit(Strategy s) {
    logger.trace("  UnBuilding strategy [" + s.getIdentifier() + "]");
    try {
      Strategy clone = s.clone();
      templateElements.put(s.getIdentifier(), clone);
      for (Support support : s.getSupports()) {
        templateElements.put(support.getIdentifier(), support.clone());
        List<String> sources =
            templateDependencies.getOrDefault(s.getIdentifier(), new ArrayList<>());
        sources.add(support.getIdentifier());
        templateDependencies.put(s.getIdentifier(), sources);
        if (support instanceof SubConclusion) {
          support.accept(this);
        }
      }

    } catch (CloneNotSupportedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void visit(Unit u) {
  }

  public void visit(AbstractSupport as) {
  }

  public void visit(Load l) {

  }

  public void visit(Evidence e) {
  }

  public Conclusion getConclusion() {
    return templateConclusion;
  }

  public Map<String, List<String>> getDependencies() {
    return templateDependencies;
  }

  public Map<String, JustificationElement> getElements() {
    return templateElements;
  }


}
