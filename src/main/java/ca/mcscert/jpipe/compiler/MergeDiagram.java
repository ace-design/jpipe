package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.compiler.builders.ConcreteJustificationBuilder;
import ca.mcscert.jpipe.compiler.builders.JustificationPatternBuilder;
import ca.mcscert.jpipe.compiler.builders.ScopedContextBuilder;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.JustificationElement;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.visitors.UnBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Merging a given list of diagrams.
 */
public class MergeDiagram {
  Tracker tracker = new Tracker();

  ScopedContextBuilder builder;
  UnBuilder unBuilder;
  Map<String, JustificationElement> elements = new HashMap<>();
  String name;
  Conclusion conclusion;

  Map<String, Map<String, JustificationElement>> allElements = new HashMap<>();
  Map<String, Map<String, List<String>>> allDependencies = new HashMap<>();


  /**
   * Constructor for this justification builder.
   *
   * @param diagramName the name of the justification diagram to be built.
   */
  public MergeDiagram(String diagramName, List<JustificationDiagram> diagrams) {
    this.conclusion = diagrams.get(0).conclusion();
    this.name = diagramName;
    mergeDiagrams(diagrams);
  }

  public ScopedContextBuilder getDiagram() {
    return this.builder;
  }

  private void mergeDiagrams(List<JustificationDiagram> diagrams) {
    this.conclusion = diagrams.get(0).conclusion();
    boolean isPattern = false;
    for (JustificationDiagram diagram : diagrams) {
      if (diagram instanceof JustificationPattern) {
        isPattern = true;
      }
      unBuilder = new UnBuilder();
      trackDiagram(unBuilder, diagram);
    }
    if (isPattern) {
      builder = new JustificationPatternBuilder(this.name);
    } else {
      builder = new ConcreteJustificationBuilder(this.name);
    }
    setElements();
    tracker.addDiagram(this.name, this.elements);
    setDependencies();
  }

  private void setDependencies() {
    for (String diagram : allDependencies.keySet()) {
      for (String start : allDependencies.get(diagram).keySet()) {
        String newDiagramIdentifier;
        JustificationElement element = allElements.get(diagram).get(start);
        newDiagramIdentifier = tracker.getIdentifier(element, this.name);

        for (String end : allDependencies.get(diagram).get(start)) {
          element = allElements.get(diagram).get(end);
          String newEndDiagramIdentifier = tracker.getIdentifier(element, this.name);
          builder.addDependency(newEndDiagramIdentifier, newDiagramIdentifier);
        }
      }

    }
  }

  private void setElements() {
    Set<JustificationElement> elements = tracker.getUniqueElements();
    for (JustificationElement element : elements) {
      if (builder.containsIdentifier(element.getIdentifier())) {
        //Refactor later
        element.updateIdentifier(element.getIdentifier() + '1');
      }
      if (element instanceof Conclusion) {
        builder.setConclusion((Conclusion) element);
      }
      builder.addElement(element);
      this.elements.put(element.getIdentifier(), element);
    }
  }


  /**
   * Extract elements, and dependencies from given diagram. Also allow give tracker that data.
   *
   * @param unBuilder new unbuilder for every given diagram.
   * @param diagram   diagram to be unbuilt and extracted.
   */
  public void trackDiagram(UnBuilder unBuilder, JustificationDiagram diagram) {
    if (diagram.conclusion().equals(conclusion)) {
      diagram.accept(unBuilder);
      Map<String, JustificationElement> diagramElements = unBuilder.getElements();
      Conclusion diagramConclusion = unBuilder.getConclusion();
      diagramElements.put(diagramConclusion.getIdentifier(), diagramConclusion);
      allElements.put(diagram.name(), diagramElements);
      allDependencies.put(diagram.name(), unBuilder.getDependencies());
      tracker.addDiagram(diagram.name(), unBuilder.getElements());
    } else {
      throw new RuntimeException("Diagrams cannot have different conclusions.");
    }

  }


}
