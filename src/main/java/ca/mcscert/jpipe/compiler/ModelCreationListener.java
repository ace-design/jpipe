package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.compiler.builders.ConcreteJustificationBuilder;
import ca.mcscert.jpipe.compiler.builders.JustificationBuilder;
import ca.mcscert.jpipe.compiler.builders.JustificationPatternBuilder;
import ca.mcscert.jpipe.compiler.builders.ImplementJustificationBuilder;
import ca.mcscert.jpipe.model.*;
import ca.mcscert.jpipe.model.justification.*;
import ca.mcscert.jpipe.syntax.JPipeBaseListener;
import ca.mcscert.jpipe.syntax.JPipeParser;
import ca.mcscert.jpipe.compiler.CompilationError;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

import java.util.List;
import java.nio.file.Path;



public class ModelCreationListener extends JPipeBaseListener {

    private static Logger logger = LogManager.getLogger(ModelCreationListener.class);

    private JustificationBuilder justifBuilder;
    private final Map<String,JustificationDiagram> justifications = new HashMap<>();

    private Unit result;
    private Path fileName;
    private Compiler compiler;

    public ModelCreationListener(String fileName, Compiler compiler) {
        this.fileName = Paths.get(fileName);
        this.result = new Unit(fileName);
        this.compiler = compiler;
    }

    public Unit build() {
        for (JustificationDiagram justification: justifications.values()) {
            result.add(justification);
        }
        return result;
    }


    /** Processing a Justification **/

    @Override
    public void enterJustification(JPipeParser.JustificationContext ctx) {
        if (ctx.parent!=null){
            logger.trace("Implementation of [" + ctx.parent.getText() + "]");
                if (justifications.get(ctx.parent.getText())!=null){
                    try{
                        JustificationPattern parent_pattern=(JustificationPattern)justifications.get(ctx.parent.getText());
                        justifBuilder = new ImplementJustificationBuilder(ctx.id.getText(),parent_pattern);
                    }catch (ClassCastException e){
                        logger.trace("Cannot implement [" + ctx.parent.getText() + "] since it is not a pattern");
                        throw new RuntimeException(e);        
                    }
                }else{
                    throw new CompilationError(ctx.id.getLine(), ctx.id.getStartIndex(), "Pattern does not exist");

                }
        }else{
            logger.trace("Entering Justification [" + ctx.id.getText() + "]");
            justifBuilder = new ConcreteJustificationBuilder(ctx.id.getText());
        }
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitJustification(JPipeParser.JustificationContext ctx) {
        logger.trace("Exiting Justification [" + ctx.id.getText() + "]");
        justifications.put(ctx.id.getText(),justifBuilder.build());
        justifBuilder = null;
    }

    /** Processing a pattern **/

    @Override
    public void enterPattern(JPipeParser.PatternContext ctx) {
        logger.trace("Entering Pattern [" + ctx.id.getText() + "]");
        justifBuilder = new JustificationPatternBuilder(ctx.id.getText());
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    }

    @Override
    public void exitPattern(JPipeParser.PatternContext ctx) {
        logger.trace("Exiting Pattern [" + ctx.id.getText() + "]");
        justifications.put(ctx.id.getText(),justifBuilder.build());
        justifBuilder = null;
    }

    /** Processing a Relation **/

    @Override
    public void enterRelation(JPipeParser.RelationContext ctx) {
        logger.trace("  Processing Relation [" + ctx.from.getText() + "->" + ctx.to.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        justifBuilder.addDependency(ctx.from.getText(), ctx.to.getText());
    }

    /** Processing justification diagram elements **/

    @Override
    public void enterEvidence(JPipeParser.EvidenceContext ctx) {
        logger.trace("  Processing Evidence [" + ctx.identified_element().id.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        Evidence evidence = new Evidence(ctx.identified_element().id.getText(),
                                         clean(ctx.identified_element().name.getText()));
        justifBuilder.addElement(evidence);
    }

    @Override
    public void enterStrategy(JPipeParser.StrategyContext ctx) {
        logger.trace("  Processing Strategy [" + ctx.identified_element().id.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        Strategy strategy = new Strategy(ctx.identified_element().id.getText(),
                                         clean(ctx.identified_element().name.getText()));
        justifBuilder.addElement(strategy);
    }

    @Override
    public void enterSub_conclusion(JPipeParser.Sub_conclusionContext ctx) {
        logger.trace("  Processing Sub-Conclusion [" + ctx.identified_element().id.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        SubConclusion subconclusion = new SubConclusion(ctx.identified_element().id.getText(),
                                                        clean(ctx.identified_element().name.getText()));
        justifBuilder.addElement(subconclusion);
    }


    @Override
    public void enterConclusion(JPipeParser.ConclusionContext ctx) {
        logger.trace("  Processing Conclusion [" + ctx.identified_element().id.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        Conclusion conclusion = new Conclusion(ctx.identified_element().id.getText(),
                                                clean(ctx.identified_element().name.getText()));
        justifBuilder.setConclusion(conclusion);
        justifBuilder.addElement(conclusion);
    }


    @Override
    public void enterAbs_support(JPipeParser.Abs_supportContext ctx) {
        logger.trace("  Processing Abstract Support [" + ctx.identified_element().id.getText() + "]");
        justifBuilder.updateLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        AbstractSupport evidence = new AbstractSupport(ctx.identified_element().id.getText(),
                clean(ctx.identified_element().name.getText()));
        justifBuilder.addElement(evidence);
    }

    /** Processing load element **/
    @Override
    public void enterLoad(JPipeParser.LoadContext ctx) {
        Path loadPath=Paths.get(ctx.file.getText().replace("\"",""));

        if (compiler.isCompiled(loadPath)){
            logger.trace("  Already entered ["+loadPath.getFileName()+"]");
        }else{
            logger.trace("  Entering Load ["+loadPath.getFileName()+"]");
            Load loadFile=new Load(loadPath,fileName);
            try {
                Unit unit = compiler.compile(loadFile.getLoadPath());
                for (JustificationDiagram j:unit.getJustificationSet()){
                    justifications.put(j.name(),j);
                }
                result.merge(unit);
            } catch (FileNotFoundException e){
                throw new RuntimeException(e);
            }
        }
    }

    private String clean(String s) {
        return s.substring(1,s.length()-1);
    }

}
