package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.actions.Action;
import ca.mcscert.jpipe.compiler.model.ChainBuilder;
import ca.mcscert.jpipe.compiler.steps.ActionListProvider;
import ca.mcscert.jpipe.compiler.steps.CharStreamProvider;
import ca.mcscert.jpipe.compiler.steps.LazyHaltAndCatchFire;
import ca.mcscert.jpipe.compiler.steps.Lexer;
import ca.mcscert.jpipe.compiler.steps.Parser;
import ca.mcscert.jpipe.compiler.steps.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Processing load statements by transforming a file into a sequence of actions.
 */
public class ActionLoader {

    private final String root;

    /**
     * A loader works relatively to a root directory.
     *
     * @param root the location of the root directory.
     */
    public ActionLoader(String root) {
        this.root = root;
    }

    /**
     * Load a given file (relative to root path) as a sequence of action.
     *
     * @param fileName the file path.
     * @return the list of actions to execute.
     */
    public List<Action> load(String fileName) {
        List<Throwable> errors = new ArrayList<>();
        ChainBuilder<InputStream, List<Action>>  builder =
            new FileReader()
                .andThen(new CharStreamProvider())
                .andThen(new Lexer(errors))
                .andThen(new Parser(errors))
                .andThen(new LazyHaltAndCatchFire<>(errors))
                .andThen(new ActionListProvider());
        try {
            return builder.partialExecution(Paths.get(root, fileName).toString());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }

}
