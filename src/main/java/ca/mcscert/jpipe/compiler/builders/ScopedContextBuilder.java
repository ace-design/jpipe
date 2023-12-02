package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.compiler.CompilationError;
import ca.mcscert.jpipe.compiler.TypeError;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;
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
 * Builder pattern applied to the building of a scope (Justification diagram or pattern).
 */
public abstract class ScopedContextBuilder {

  // the name of the scope
  protected final String name;
  // set of elements in the scope, indexed by name inside the scope.
  protected Map<String, JustificationElement> elements;
  // List of dependencies between elements
  protected Map<String, List<String>> dependencies;
  // the entry point of the scope (a conclusion)
  protected Conclusion conclusion = null;
  // Current line analysed when building the element (location in the source file)
  protected int line;
  // current character analysed (location in the source file)
  protected int character;
  // Logger used to print message
  protected static Logger logger = LogManager.getLogger(ConcreteJustificationBuilder.class);

  /**
   * Common constructor used by specialized classes that rely on a scope.
   *
   * @param name the name of the justification we are building
   */
  protected ScopedContextBuilder(String name) {
    this.name = name;
    this.elements = new HashMap<>();
    this.dependencies = new HashMap<>();
    this.line = 0;
    this.character = 0;

  }

  /**
   * Update the location of the builder in the file.
   *
   * @param line      current line in the source file
   * @param character current character (column) in the source file
   */
  public void updateLocation(int line, int character) {
    this.line = line;
    this.character = character;
  }

  /**
   * Create a dependency between two elements.
   *
   * @param from the element on the left-hand side of the dependency
   * @param to   the element on the right-hand side of the dependency
   */
  public void addDependency(String from, String to) {
    if (elements.get(from) == null || elements.get(to) == null) {
      error("Cannot create relation between non existing arguments");
    }
    List<String> sources = this.dependencies.getOrDefault(to, new ArrayList<>());
    sources.add(from);
    this.dependencies.put(to, sources);
  }

  /**
   * Add a new identified element into the set of known elements.
   *
   * @param elem the justification element to be added.
   */
  public void addElement(JustificationElement elem) {

    String identifier = elem.getIdentifier();
    if (containsIdentifier(identifier)) {
      error("Cannot use the same element identifier twice [" + identifier + "]");
    }
    this.elements.put(identifier, elem);

  }

  /**
   * Determine if identifier is already being used.
   *
   * @param identifier the identifier being checked if exits.
   */
  public boolean containsIdentifier(String identifier) {
    if (this.elements.get(identifier) == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Set the entry point of the scope (its conclusion).
   *
   * @param conclusion the conclusion instance used as entry point
   */
  public void setConclusion(Conclusion conclusion) {
    if (this.conclusion != null) {
      error("A justification cannot lead to two different conclusions");
    }
    this.conclusion = conclusion;
  }

  /**
   * Throw an error to stop the process, could be compilation or type error.
   *
   * @param message the error message
   */
  protected final void error(String message) {
    if (this.line != -1) {
      throw new CompilationError(this.line, this.character, message);
    } else {
      throw new TypeError(message);
    }
  }


  /**
   * finalizing the build.
   *
   * @return an instance of a class implementing JustificationDiagram
   */
  public abstract JustificationDiagram build();

  /**
   * Check the constraints on the predecessors of a conclusion step.
   *
   * @param e the conclusion to be checked.
   */
  public abstract void checkConclusionPredecessor(JustificationElement e);

  /**
   * Check the constraints on the predecessors of a strategy.
   *
   * @param e the strategy to check
   */
  public abstract void checkStrategyPredecessor(JustificationElement e);

  /**
   * Check the constraints on a sub conclusion.
   *
   * @param e the subconclusion to check.
   */
  public abstract void checkSubConclusionPredecessor(JustificationElement e);


  protected final void fill(Conclusion c) {
    logger.trace("  Finalizing build of Conclusion [" + c.getIdentifier() + "]");
    for (String from :
        this.dependencies.getOrDefault(c.getIdentifier(), new ArrayList<>())) {
      JustificationElement source = this.elements.get(from);
      checkConclusionPredecessor(source);
      Strategy strategy = (Strategy) source;
      c.addSupport(strategy);
      fill(strategy);
    }
  }

  private final void fill(Strategy strategy) {
    logger.trace("  Finalizing build of Strategy [" + strategy.getIdentifier() + "]");
    for (String from :
        this.dependencies.getOrDefault(strategy.getIdentifier(), new ArrayList<>())) {
      JustificationElement source = this.elements.get(from);
      checkStrategyPredecessor(source);
      strategy.addSupport((Support) source);
      if (source instanceof SubConclusion) {
        fill((SubConclusion) source);
      }
    }
  }

  private final void fill(SubConclusion sc) {
    logger.trace("  Finalizing build of SubConclusion [" + sc.getIdentifier() + "]");
    for (String from : this.dependencies.getOrDefault(sc.getIdentifier(), new ArrayList<>())) {
      JustificationElement source = this.elements.get(from);
      checkSubConclusionPredecessor(source);
      Strategy strategy = (Strategy) source;
      sc.addSupport(strategy);
      fill(strategy);
    }
  }


}
