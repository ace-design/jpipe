package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;

/**
 * This class structure what a support can be in a justification.
 */
public abstract class Support extends JustificationElement implements Visitable, Cloneable {

  public Support(String identifier, String label) {
    super(identifier, label);
  }

  @Override
  public Support clone() throws CloneNotSupportedException {
    Support clonedSupport = (Support) super.clone();
    return clonedSupport;
  }

}
