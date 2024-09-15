package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.actions.Action;
import ca.mcscert.jpipe.actions.CreateAbstractSupport;
import ca.mcscert.jpipe.actions.CreateConclusion;
import ca.mcscert.jpipe.actions.CreateEvidence;
import ca.mcscert.jpipe.actions.CreateJustification;
import ca.mcscert.jpipe.actions.CreatePattern;
import ca.mcscert.jpipe.actions.CreateRelation;
import ca.mcscert.jpipe.actions.CreateStrategy;
import ca.mcscert.jpipe.actions.CreateSubConclusion;
import ca.mcscert.jpipe.actions.LoadFile;
import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.syntax.JPipeBaseListener;
import ca.mcscert.jpipe.syntax.JPipeParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Create a list of actions to build a model out of a parse tree.
 */
public final class ActionListProvider extends Transformation<ParseTree, List<Action>> {

    /**
     * Transform a ParseTree into a list of actions to be executed to build the model.
     *
     * @param input the parse tree to visit.
     * @param source the name of the file that provided the parse tree (error mgmt)
     * @return the ordered list of Action instances to execute to create the model
     * @throws Exception is something goes wrong
     */
    @Override
    protected List<Action> run(ParseTree input, String source) throws Exception {
        ActionBuilder ab = new ActionBuilder(source);
        ParseTreeWalker.DEFAULT.walk(ab, input);
        return ab.collect();
    }

    /**
     * Creating the actions is a visit of the raw parse tree provided by ANTLR.
     */
    private static class ActionBuilder extends JPipeBaseListener {

        private record Context(String unitFileName, String justificationId) {
            public Context updateCurrentJustification(String id) {
                return new Context(this.unitFileName, id);
            }
        }

        private final List<Action> result;
        private Context buildContext;

        public ActionBuilder(String name) {
            this.result = new ArrayList<>();
            this.buildContext = new Context(name, null);
        }

        public List<Action> collect() {
            return result;
        }

        @Override
        public void enterLoad(JPipeParser.LoadContext ctx) {
            String relativePath = normalizePath(this.buildContext.unitFileName,
                                            strip(ctx.path.getText()));
            result.add(new LoadFile(relativePath));
        }

        @Override
        public void enterJustification(JPipeParser.JustificationContext ctx) {
            this.buildContext = buildContext.updateCurrentJustification(ctx.id.getText());
            if (ctx.parent == null) {
                result.add(new CreateJustification(buildContext.unitFileName, ctx.id.getText()));
            } else {
                result.add(new CreateJustification(buildContext.unitFileName,
                                                    ctx.id.getText(), ctx.parent.getText()));
            }
        }

        @Override
        public void exitJustification(JPipeParser.JustificationContext ctx) {
            this.buildContext = buildContext.updateCurrentJustification(null);
        }

        @Override
        public void enterPattern(JPipeParser.PatternContext ctx) {
            this.buildContext = buildContext.updateCurrentJustification(ctx.id.getText());
            if (ctx.parent == null) {
                result.add(new CreatePattern(buildContext.unitFileName, ctx.id.getText()));
            } else {
                result.add(new CreatePattern(buildContext.unitFileName,
                                                ctx.id.getText(), ctx.parent.getText()));
            }
        }

        @Override
        public void exitPattern(JPipeParser.PatternContext ctx) {
            this.buildContext = buildContext.updateCurrentJustification(null);
        }

        @Override
        public void enterEvidence(JPipeParser.EvidenceContext ctx) {
            String identifier = ctx.element().id.getText();
            result.add(new CreateEvidence(buildContext.justificationId,
                        identifier, strip(ctx.element().name.getText())));
        }

        @Override
        public void enterAbstract(JPipeParser.AbstractContext ctx) {
            String identifier = ctx.element().id.getText();
            result.add(new CreateAbstractSupport(buildContext.justificationId,
                    identifier, strip(ctx.element().name.getText())));
        }

        @Override
        public void enterStrategy(JPipeParser.StrategyContext ctx) {
            String identifier = ctx.element().id.getText();
            result.add(new CreateStrategy(buildContext.justificationId,
                        identifier, strip(ctx.element().name.getText())));
        }

        @Override
        public void enterConclusion(JPipeParser.ConclusionContext ctx) {
            String identifier = ctx.element().id.getText();
            result.add(new CreateConclusion(buildContext.justificationId,
                    identifier, strip(ctx.element().name.getText())));
        }

        @Override
        public void enterSub_conclusion(JPipeParser.Sub_conclusionContext ctx) {
            String identifier = ctx.element().id.getText();
            result.add(new CreateSubConclusion(buildContext.justificationId,
                    identifier, strip(ctx.element().name.getText())));
        }

        @Override
        public void enterRelation(JPipeParser.RelationContext ctx) {
            result.add(new CreateRelation(buildContext.justificationId,
                    ctx.from.getText(), ctx.to.getText()));
        }

        private String strip(String s) {
            return s.substring(1, s.length() - 1);
        }

        private String normalizePath(String rootFile, String fileName) {
            Path root = Paths.get(rootFile).getParent();
            Path target = Paths.get(fileName);
            return root.resolve(target).toString();
        }

    }

}
