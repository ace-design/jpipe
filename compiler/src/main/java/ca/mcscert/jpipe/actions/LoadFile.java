package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.compiler.CompilerFactory;
import ca.mcscert.jpipe.compiler.model.Source;
import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.compiler.steps.io.FileReader;
import ca.mcscert.jpipe.error.SemanticError;
import ca.mcscert.jpipe.model.Unit;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Load a file referenced in another file.
 */
public class LoadFile implements Action {

    private static final Logger logger = LogManager.getLogger();

    private final String fileName;

    public LoadFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute(Unit context) throws Exception {
        Path p = new File(fileName).toPath().normalize();
        canLoad(p, context);
        context.addLoadedFile(p);
        Transformation<InputStream, List<Action>> partial = CompilerFactory.actionProvider();
        Source<InputStream> reader = new FileReader();
        try (InputStream in = reader.provideFrom(p.toString())) {
            List<Action> actions = partial.fire(in, p.toString());
            ExecutionEngine engine = new ExecutionEngine();
            engine.enrich(context, actions);
        }
    }


    private void canLoad(Path p, Unit context) {

        if (context.isInScope(p)) {
            throw new SemanticError("Cannot load file " + p + " as it is already loaded");
        }
        if (!p.toFile().exists()) {
            throw new SemanticError("Cannot load file " + p + " (file not found or not readable)");
        }
    }

}
