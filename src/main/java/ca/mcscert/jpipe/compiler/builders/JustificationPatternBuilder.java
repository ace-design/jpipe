package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.Support;

/**
 * Concrete implementation of a ScopedContextBuilder targeting Justification Patterns.
 */
public final class JustificationPatternBuilder extends ScopedContextBuilder {

  public JustificationPatternBuilder(String name) {
    super(name);
  }

  /**
   * Build method to finalize the build (Builder pattern).
   *
   * @return a Justification Pattern
   */
  public JustificationDiagram build() {
    this.line = -1;
    this.character = -1;
    logger.trace("Finalizing build of Pattern [" + this.name + "]");
    JustificationDiagram result = new JustificationPattern(this.name, this.conclusion);
    fill(this.conclusion);
    logger.trace("Finalization complete! [" + this.name + "]");
    return result;
  }

  @Override
  public void checkConclusionPredecessor(JustificationElement e) {
    if (!(e instanceof Strategy)) {
      error("A Conclusion can only be supported by strategies, but ["
          + e.getIdentifier() + "] is not a Strategy)");
    }
  }

  @Override
  public void checkStrategyPredecessor(JustificationElement e) {
    if (!(e instanceof Support)) {
      error("A Strategy can only be supported by a sub-conclusion, an evidence, "
          + "or an abstract support but [" + e.getIdentifier() + "] is a "
          + e.getClass().getCanonicalName() + " )");
    }
  }

  @Override
  public void checkSubConclusionPredecessor(JustificationElement e) {
    if (!(e instanceof Strategy)) {
      error("A Conclusion can only be supported by strategies, but ["
          + e.getIdentifier() + "] is not a Strategy)");
    }
  }
}
