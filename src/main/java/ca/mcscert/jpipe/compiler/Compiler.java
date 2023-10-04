package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.syntax.JPipeLexer;
import ca.mcscert.jpipe.syntax.JPipeParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


/**
 * Compiler class that produces an AST (Unit model element) out of a given file.
 */
public final class Compiler {

    // the set of filed that were compiled (through load directives), to avoid cycles.
    private final Set<String> compiled = new HashSet<>();

    /**
     * Entry point of the compiler, triggering the compilation process.
     * This is a shortcut to the method that uses a charstream.
     *
     * @param fileName name of the file to compile.
     * @return a Unit model element modelling the AST of the compiled file
     * @throws FileNotFoundException if file does not exist
     */
    public Unit compile(String fileName) throws FileNotFoundException {
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            return this.compile(input, fileName);
        } catch (IOException e) {
            throw new FileNotFoundException(fileName);
        }
    }

    /**
     * Compiler entry point, reading a charStream from a given file to produce an Unit.
     *
     * @param stream the characters stream to read.
     * @param fileName the filename used as input for this stream
     * @return the Unit element containing the AST of the file
     */
    public Unit compile(CharStream stream, String fileName) {
        this.compiled.add(Paths.get(fileName).getFileName().toString());
        JPipeLexer lexer = new JPipeLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JPipeParser parser = new JPipeParser(tokens);
        ParseTree tree = parser.unit();
        ModelCreationListener builder = new ModelCreationListener(fileName, this);
        ParseTreeWalker.DEFAULT.walk(builder, tree);
        return builder.build();
    }

    /**
     * Package-visible method to check if a file was already compiled or not (avoiding cycles).
     *
     * @param fileName the path to the file one wants to check
     * @return true if this very file was already compiled.
     */
    Boolean isCompiled(Path fileName) {
        String file = fileName.getFileName().toString();
        return compiled.contains(file);
    }

}
