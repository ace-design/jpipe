package ca.mcscert.jpipe.compiler.builders;

import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.visitors.UnBuilder;
import java.util.ArrayList;
import java.util.List;


/**
 * Extension of a ConcreteJustificationBuilder targeting implementations of JustificationPatterns.
 */
public class ImplementJustificationBuilder extends ConcreteJustificationBuilder {

  private final JustificationPattern template;

  private final UnBuilder unbuilder = new UnBuilder();

  /**
   * Instantiate the new diagram builder.
   *
   * @param name the identifier for the justification diagram implementation
   * @param template the Pattern object to be implemented
   */
  public ImplementJustificationBuilder(String name, JustificationPattern template) {
    super(name);
    this.template = template;
    extract();
  }

  private void extract() {
    template.accept(unbuilder);
    this.elements = unbuilder.getElements();
    this.dependencies = unbuilder.getDependencies();
    setConclusion(unbuilder.getConclusion());
  }

  @Override
  public void addElement(JustificationElement elem) {
    String identifier = elem.getIdentifier();
    this.elements.put(identifier, elem);
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
