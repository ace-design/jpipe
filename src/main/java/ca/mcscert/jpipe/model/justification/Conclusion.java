package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a Conclusion node in the AST.
 */
public class Conclusion extends JustificationElement implements Visitable, Cloneable {

  // The set of supporting strategies.
  private Set<Strategy> supports = new HashSet<>();

  public Conclusion(String identifier, String label) {
    super(identifier, label);
  }

  /**
   * Add a supporting strategy to that conclusion.
   *
   * @param from the strategy supporting the conclusion.
   */
  public void addSupport(Strategy from) {
    this.supports.add(from);
  }

  /**
   * Return the set of supporting strategies for that conclusion.
   *
   * @return the aforementioned set.
   */
  public Set<Strategy> getSupports() {
    return supports;
  }

  @Override
  public void accept(AbstractVisitor<?> visitor) {
    visitor.visit(this);
  }

  @Override
  public Conclusion clone() throws CloneNotSupportedException {
    Conclusion clonedConclusion = (Conclusion) super.clone();
    clonedConclusion.supports = new HashSet<>();
    return clonedConclusion;
  }
}
