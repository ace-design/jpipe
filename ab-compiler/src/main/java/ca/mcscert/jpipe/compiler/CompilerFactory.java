package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.cli.Configuration;
import ca.mcscert.jpipe.compiler.model.ChainCompiler;
import ca.mcscert.jpipe.compiler.model.Source;
import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.compiler.steps.HaltAndCatchFire;
import ca.mcscert.jpipe.compiler.steps.ActionListInterpretation;
import ca.mcscert.jpipe.compiler.steps.ActionListProvider;
import ca.mcscert.jpipe.compiler.steps.CharStreamProvider;
import ca.mcscert.jpipe.compiler.steps.CompletenessChecker;
import ca.mcscert.jpipe.compiler.steps.Lexer;
import ca.mcscert.jpipe.compiler.steps.ModelVisit;
import ca.mcscert.jpipe.compiler.steps.Parser;
import ca.mcscert.jpipe.compiler.steps.ScopeFiltering;
import ca.mcscert.jpipe.compiler.steps.io.FileReader;
import ca.mcscert.jpipe.compiler.steps.io.GraphVizRenderer;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.visitors.exporters.MutableGraphExporter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class CompilerFactory {

    public static Compiler build(Configuration config) {
        return defaultCompiler(config);
    }

    public static Compiler defaultCompiler(Configuration config) {
        List<Throwable> errors = new ArrayList<>();

        Source<InputStream> source = new FileReader();

        return source.andThen(new CharStreamProvider())
                     .andThen(new Lexer(errors))
                     .andThen(new Parser(errors))
                     .andThen(new HaltAndCatchFire<>(errors))
                     .andThen(new ActionListProvider())
                     .andThen(new ActionListInterpretation())
                     .andThen(new CompletenessChecker())
                     .andThen(new ScopeFiltering(config.getDiagramName()))
                     .andThen(new ModelVisit<>(new MutableGraphExporter()))
                     .andThen(new GraphVizRenderer(config));
    }


    /*
    private static class TransformationChain implements ChainCompiler {

        private final Transformation<String, Unit> chain;

        public TransformationChain() {
            Source<InputStream> start = new FileReader();
            List<Throwable> errors = new ArrayList<>();
            this.chain =
                    start.andThen(new CharStreamProvider())
                    .andThen(new Lexer(errors))
                    .andThen(new Parser(errors))
                    .andThen(new HaltAndCatchFire<>(errors))
                    .andThen(new ActionListProvider())
                    .andThen(new ActionListInterpretation())
                    .andThen(new CompletenessChecker());


        }

        @Override
        public void compile(String fileName) throws IOException {
            this.chain.fire(fileName, fileName);
        }
    }

*/


    /*
    private static class DefaultCompiler implements ChainCompiler {

        private final Configuration config;

        public DefaultCompiler(Configuration config) {
            this.config = config;
        }

        @Override
        public void compile(String fileName) throws IOException {
            Source<InputStream> chain = assemble(fileName);
            chain.fire(fileName, fileName);
        }

        private Source<InputStream> assemble(String fileName) {
            // Collecting errors while lexing/parsing, aborting when all errors are collected.
            List<Throwable> errors = new ArrayList<>();

            Source<InputStream> source = new FileReader();

            source.andThen(new CharStreamProvider())
                    .andThen(new Lexer(errors))
                    .andThen(new Parser(errors))
                    .andThen(new HaltAndCatchFire<>(errors))
                    .andThen(new ActionListProvider())
                    .andThen(new ActionListInterpretation())
                    .andThen(new CompletenessChecker())
                    .andThen(new ScopeFiltering(this.config.getDiagramName()))
                    .andThen(new ModelVisit<>(new MutableGraphExporter()))
                    .andThen(new GraphVizRenderer(config))
            ;
            return source;
        }

    }


     */
}
