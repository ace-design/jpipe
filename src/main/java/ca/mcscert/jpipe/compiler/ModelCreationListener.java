package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.compiler.builders.ConcreteJustificationBuilder;
import ca.mcscert.jpipe.compiler.builders.ImplementJustificationBuilder;
import ca.mcscert.jpipe.compiler.builders.JustificationPatternBuilder;
import ca.mcscert.jpipe.compiler.builders.MergeBuilder;
import ca.mcscert.jpipe.compiler.builders.ScopedContextBuilder;
import ca.mcscert.jpipe.model.JustificationDiagram;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.justification.AbstractSupport;
import ca.mcscert.jpipe.model.justification.Conclusion;
import ca.mcscert.jpipe.model.justification.Evidence;
import ca.mcscert.jpipe.model.justification.JustificationPattern;
import ca.mcscert.jpipe.model.justification.Load;
import ca.mcscert.jpipe.model.justification.Strategy;
import ca.mcscert.jpipe.model.justification.SubConclusion;
import ca.mcscert.jpipe.syntax.JPipeBaseListener;
import ca.mcscert.jpipe.syntax.JPipeParser;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Implementation of the ANTLR-generated listener to create the model representation (AST).
 * This class uses a Builder pattern => to get the compiled Unit, one needs to call the
 * `build()` method after having listened to the parsing.
 */
public class ModelCreationListener extends JPipeBaseListener {

  private static Logger logger = LogManager.getLogger(ModelCreationListener.class);

  private ScopedContextBuilder justifBuilder;
  private final Map<String, JustificationDiagram> justifications = new HashMap<>();

  private Unit result;
  private Path fileName;
  private Compiler compiler;
  private MergeBuilder mergeBuilder;

  /**
   * Instantiate the listener, referring to the compiler (to avoid load cycles) and the filename.
   *
   * @param fileName path to the file used to compile
   * @param compiler the compiler instance used to compile that file
   */
  public ModelCreationListener(String fileName, Compiler compiler) {
    this.fileName = Paths.get(fileName);
    this.result = new Unit(fileName);
    this.compiler = compiler;
  }

  /**
   * Finalize the build of the justifications.
   *
   * @return the Unit model element containing the result of the compilation process.
   */
  public Unit build() {
    for (JustificationDiagram justification : justifications.values()) {
      result.add(justification);
    }
    return result;
  }

  /***********************************************************
   * Processing high level elements (justification, pattern) *
   ***********************************************************/

  /**
   * entering a justification pattern, setting up the scope.
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterJustification(JPipeParser.JustificationContext ctx) {
    if (ctx.parent != null) {
      logger.trace("Implementation of [" + ctx.parent.getText() + "]");
      if (justifications.get(ctx.parent.getText()) != null) {
        try {
          JustificationPattern parentPattern =
              (JustificationPattern) justifications.get(ctx.parent.getText());
          justifBuilder = new ImplementJustificationBuilder(ctx.id.getText(), parentPattern);
        } catch (ClassCastException e) {
          logger.trace("Cannot implement [" + ctx.parent.getText() + "] since it is not a pattern");
          throw new RuntimeException(e);
        }
      } else {
        throw new CompilationError(ctx.id.getLine(), ctx.id.getStartIndex(),
            "Pattern does not exist");
      }
    } else {
      logger.trace("Entering Justification [" + ctx.id.getText() + "]");
      justifBuilder = new ConcreteJustificationBuilder(ctx.id.getText());
    }
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
  }

  /**
   * Exiting a justification, releasing the scope.
   *
   * @param ctx the parse tree
   */
  @Override
  public void exitJustification(JPipeParser.JustificationContext ctx) {
    logger.trace("Exiting Justification [" + ctx.id.getText() + "]");
    justifications.put(ctx.id.getText(), justifBuilder.build());
    justifBuilder = null;
  }

  /**
   * Entering a pattern.
   * We set the scope for the next elements to be enclosed in this pattern.
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterPattern(JPipeParser.PatternContext ctx) {
    logger.trace("Entering Pattern [" + ctx.id.getText() + "]");
    justifBuilder = new JustificationPatternBuilder(ctx.id.getText());
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
  }

  /**
   * Exiting a pattern, releasing the scope.
   *
   * @param ctx the parse tree
   */
  @Override
  public void exitPattern(JPipeParser.PatternContext ctx) {
    logger.trace("Exiting Pattern [" + ctx.id.getText() + "]");
    justifications.put(ctx.id.getText(), justifBuilder.build());
    justifBuilder = null;
  }

  /************************
   * Processing relations *
   ************************/

  /**
   * Process a relation between elements in the justification diagram.
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterRelation(JPipeParser.RelationContext ctx) {
    logger.trace("  Processing Relation [" + ctx.from.getText() + "->" + ctx.to.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    justifBuilder.addDependency(ctx.from.getText(), ctx.to.getText());
  }

  /*********************************************
   * Processing justification diagram elements *
   *********************************************/

  /**
   * Process an evidence node (evidence).
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterEvidence(JPipeParser.EvidenceContext ctx) {
    logger.trace("  Processing Evidence [" + ctx.identified_element().id.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    Evidence evidence = new Evidence(ctx.identified_element().id.getText(),
        clean(ctx.identified_element().name.getText()));
    justifBuilder.addElement(evidence);
  }

  /**
   * Process a strategy node (strategy).
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterStrategy(JPipeParser.StrategyContext ctx) {
    logger.trace("  Processing Strategy [" + ctx.identified_element().id.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    Strategy strategy = new Strategy(ctx.identified_element().id.getText(),
        clean(ctx.identified_element().name.getText()));
    justifBuilder.addElement(strategy);
  }

  /**
   * Process a sub conclusion node (subconclusion).
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterSub_conclusion(JPipeParser.Sub_conclusionContext ctx) {
    logger.trace("  Processing Sub-Conclusion [" + ctx.identified_element().id.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    SubConclusion subconclusion = new SubConclusion(ctx.identified_element().id.getText(),
        clean(ctx.identified_element().name.getText()));
    justifBuilder.addElement(subconclusion);
  }

  /**
   * Process a conclusion node (conclusion).
   *
   * @param ctx the parse tree
   */
  @Override
  public void enterConclusion(JPipeParser.ConclusionContext ctx) {
    logger.trace("  Processing Conclusion [" + ctx.identified_element().id.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    Conclusion conclusion = new Conclusion(ctx.identified_element().id.getText(),
        clean(ctx.identified_element().name.getText()));
    justifBuilder.setConclusion(conclusion);
    justifBuilder.addElement(conclusion);
  }


  /**
   * Processing an abstract support node (@support).
   *
   * @param ctx the parse tree provided by ANTLR
   */
  @Override
  public void enterAbs_support(JPipeParser.Abs_supportContext ctx) {
    logger.trace("  Processing Abstract Support [" + ctx.identified_element().id.getText() + "]");
    justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    AbstractSupport evidence = new AbstractSupport(ctx.identified_element().id.getText(),
        clean(ctx.identified_element().name.getText()));
    justifBuilder.addElement(evidence);
  }

  /**
   * Processing a load node (load).
   *
   * @param ctx the parse tree provided by ANTLR
   **/
  @Override
  public void enterLoad(JPipeParser.LoadContext ctx) {
    Path loadPath = Paths.get(ctx.file.getText().replace("\"", ""));

    if (compiler.isCompiled(loadPath)) {

      logger.trace("  Already entered [" + loadPath.getFileName() + "]");

    } else {
      logger.trace("  Entering Load [" + loadPath.getFileName() + "]");

      Load loadFile = new Load(loadPath, fileName);
      try {
        Unit unit = compiler.compile(loadFile.getLoadPath());
        for (JustificationDiagram j : unit.getJustificationSet()) {
          justifications.put(j.name(), j);
        }
        result.merge(unit);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }


    /**
     * Processing a composition unit.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterComposition(JPipeParser.CompositionContext ctx) {
        logger.trace("Entering composition unit " + ctx.name.getText());
    }

    @Override
    public void exitComposition(JPipeParser.CompositionContext ctx) {
        logger.trace("Exiting composition unit " + ctx.name.getText());
    }

  @Override
  public void enterMerge_directive(JPipeParser.Merge_directiveContext ctx) {
    logger.trace("  Entering merge unit " + ctx.id.getText());
    List<JustificationDiagram> list =
        new ArrayList<JustificationDiagram>(this.justifications.values());
    this.mergeBuilder = new MergeBuilder(ctx.id.getText(), list);

  }

    @Override
    public void exitMerge_directive(JPipeParser.Merge_directiveContext ctx) {
        logger.trace("  Finalizing build of " + ctx.id.getText());
        this.justifications.put(ctx.id.getText(), this.mergeBuilder.build());
        this.mergeBuilder = null;
    }


    @Override
    public void enterMerge_equation(JPipeParser.Merge_equationContext ctx) {
        this.mergeBuilder.register(ctx.left.getText());
        if (ctx.right != null) {
            this.mergeBuilder.register(ctx.right.getText());
        }
    }

    /**
     * Clean up a string by removing the first and last characters. this is useful to get the
     * contents of a quoted string for example.
     *
     * @param s a given string, e.g., "foo"
     * @return the contents of the string without 1st and last character (here, foo)
     */
    private String clean(String s) {
        return s.substring(1, s.length() - 1);
    }

}
