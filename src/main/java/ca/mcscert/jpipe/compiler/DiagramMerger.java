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
public class DiagramMerger {
  // tracker in charge of referencing justification elements across diagrams being merged.
  private final Tracker tracker = new Tracker();

  // builder used to build the merged diagram.
  private ScopedContextBuilder builder;

  // elements used to track what new elements exist in merged diagram.
  Map<String, JustificationElement> elements = new HashMap<>();

  // name of merged diagram.
  private final String name;
  private Conclusion conclusion;

  // Diagram merger should be in the knowing of all merged elements and dependencies.
  private final Map<String, Map<String, JustificationElement>> allElements = new HashMap<>();
  private final Map<String, Map<String, List<String>>> allDependencies = new HashMap<>();


  /**
   * Constructor for this justification builder.
   *
   * @param diagramName the name of the justification diagram to be built.
   */
  public DiagramMerger(String diagramName, List<JustificationDiagram> diagrams) {
    this.conclusion = diagrams.get(0).conclusion();
    this.name = diagramName;
    mergeDiagrams(diagrams);
  }

  private void mergeDiagrams(List<JustificationDiagram> diagrams) {
    // All conclusions should be the same regardless.
    this.conclusion = diagrams.get(0).conclusion();

    // Check if there is at least one pattern, meaning merged diagram should be pattern.
    boolean isPattern = false;


    for (JustificationDiagram diagram : diagrams) {
      if (diagram instanceof JustificationPattern) {
        isPattern = true;
      }
      trackDiagram(new UnBuilder(), diagram);
    }

    // Initialize builder based on pattern or not.
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
    //Iterate through dependencies of all diagrams.
    for (String diagram : allDependencies.keySet()) {

      //Iterate through all "resulting" elements of dependencies for each diagram.
      //For example: C=[S1,S2] would iterate through C.
      for (String start : allDependencies.get(diagram).keySet()) {
        //Obtain the associated JustificationElement of that start identifier and diagram.
        JustificationElement element = allElements.get(diagram).get(start);

        //Cross reference using tracker to see what it is called in the new merged diagram.
        String newDiagramIdentifier = tracker.getIdentifier(element, this.name);


        for (String end : allDependencies.get(diagram).get(start)) {
          //Same idea as above but for "leading" elements for each dependency.
          element = allElements.get(diagram).get(end);
          String newEndDiagramIdentifier = tracker.getIdentifier(element, this.name);

          //Creates the new dependency.
          builder.addDependency(newEndDiagramIdentifier, newDiagramIdentifier);
        }
      }

    }
  }

  private void setElements() {
    Set<JustificationElement> elements = tracker.getUniqueElements();

    //Adds all unique elements to desired element list of merged diagram.
    //Also ensures that all element identifiers are unique.

    for (JustificationElement element : elements) {
      if (builder.containsIdentifier(element.getIdentifier())) {
        //Refactor later: Need to introduce a way to ask builder to return a new (unused) identifier
        element.updateIdentifier(element.getIdentifier() + '*');
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
      //Unbuild the diagram.
      diagram.accept(unBuilder);

      Map<String, JustificationElement> diagramElements = unBuilder.getElements();
      Conclusion diagramConclusion = unBuilder.getConclusion();
      diagramElements.put(diagramConclusion.getIdentifier(), diagramConclusion);

      //Track the diagram.
      allElements.put(diagram.name(), diagramElements);
      allDependencies.put(diagram.name(), unBuilder.getDependencies());
      tracker.addDiagram(diagram.name(), unBuilder.getElements());
    } else {
      throw new RuntimeException("Diagrams cannot have different conclusions.");
    }

  }

  public ScopedContextBuilder getDiagram() {
    return this.builder;
  }


}
