package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.actions.Action;
import ca.mcscert.jpipe.cli.Configuration;
import ca.mcscert.jpipe.compiler.model.ChainBuilder;
import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.compiler.steps.ActionListInterpretation;
import ca.mcscert.jpipe.compiler.steps.ActionListProvider;
import ca.mcscert.jpipe.compiler.steps.CharStreamProvider;
import ca.mcscert.jpipe.compiler.steps.CompletenessChecker;
import ca.mcscert.jpipe.compiler.steps.ConsistencyChecker;
import ca.mcscert.jpipe.compiler.steps.LazyHaltAndCatchFire;
import ca.mcscert.jpipe.compiler.steps.Lexer;
import ca.mcscert.jpipe.compiler.steps.ModelVisit;
import ca.mcscert.jpipe.compiler.steps.Parser;
import ca.mcscert.jpipe.compiler.steps.ScopeFiltering;
import ca.mcscert.jpipe.compiler.steps.io.FileReader;
import ca.mcscert.jpipe.compiler.steps.io.FileWriter;
import ca.mcscert.jpipe.compiler.steps.io.GraphVizRenderer;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import ca.mcscert.jpipe.visitors.exporters.GraphVizExporter;
import ca.mcscert.jpipe.visitors.exporters.JpipeExporter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory pattern to assemble steps, sink and sources into consistent compilers, based on the
 * provided configuration info from the command-line.
 */
public final class CompilerFactory {

    /**
     * Build a tailored compiler out of a provided configuration.
     *
     * @param config the configuration obtained from the command line.
     * @return the expected compiler.
     */
    public static Compiler build(Configuration config) {

        ChainBuilder<InputStream, Unit> unitBuilder = minimalChain()
                .andThen(new ActionListInterpretation())
                .andThen(new CompletenessChecker())
                .andThen(new ConsistencyChecker());

        return switch (config.getMode()) {
            case PRINT -> processPrintMode(unitBuilder, config);
            case LIST  -> processListMode(unitBuilder, config);
        };

    }

    private static Compiler processPrintMode(ChainBuilder<InputStream, Unit> unitBuilder,
                                             Configuration config) {
        ChainBuilder<InputStream, JustificationModel> chain =
                unitBuilder.andThen(new ScopeFiltering(config.getDiagramName()));

        return switch (config.getFormat()) {
            case PNG, DOT, SVG -> chain.andThen(new ModelVisit<>(new GraphVizExporter()))
                                        .andThen(new GraphVizRenderer(config.getFormat()));
            case JPIPE -> chain.andThen(new ModelVisit<>(new JpipeExporter()))
                    .andThen(new FileWriter());
            case JSON -> throw new UnsupportedOperationException("Json not supported");
            case RUNNER -> throw new UnsupportedOperationException("Runner not supported");
        };

    }

    private static Compiler processListMode(ChainBuilder<InputStream, Unit> unitBuilder,
                                            Configuration config) {
        throw new UnsupportedOperationException("LIST mode not supported in this version");
    }

    /**
     * Provide a default compilation chain.
     *
     * @param config the configuration provided by the user.
     * @return a default instance of Compiler.
     */
    private static Compiler defaultCompiler(Configuration config) {
        List<Throwable> errors = new ArrayList<>();

        return new FileReader()
                     .andThen(new CharStreamProvider())
                     .andThen(new Lexer(errors))
                     .andThen(new Parser(errors))
                     .andThen(new LazyHaltAndCatchFire<>(errors))
                     .andThen(new ActionListProvider())
                     .andThen(new ActionListInterpretation())
                     .andThen(new CompletenessChecker())
                     .andThen(new ConsistencyChecker())
                     .andThen(new ScopeFiltering(config.getDiagramName()))
                     .andThen(new ModelVisit<>(new GraphVizExporter()))
                     .andThen(new GraphVizRenderer(config.getFormat()));
    }


    /**
     * Provide a partial compilation chain to get actions out of a given filename.
     *
     * @return a default instance of Compiler.
     */
    public static Transformation<InputStream, List<Action>> actionProvider() {
        return minimalChain().asTransformation();
    }

    private static ChainBuilder<InputStream, List<Action>> minimalChain() {
        List<Throwable> errors = new ArrayList<>();
        return new FileReader()
                .andThen(new CharStreamProvider())
                .andThen(new Lexer(errors))
                .andThen(new Parser(errors))
                .andThen(new LazyHaltAndCatchFire<>(errors))
                .andThen(new ActionListProvider());
    }


}
