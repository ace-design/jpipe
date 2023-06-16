package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.model.CompilationUnit;
import ca.mcscert.jpipe.syntax.JPipeLexer;
import ca.mcscert.jpipe.syntax.JPipeParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Compiler {

    public CompilationUnit compile(String fileName) throws FileNotFoundException {
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            return this.compile(input);
        } catch (IOException e) {
            throw new FileNotFoundException(fileName);
        }
    }

    public CompilationUnit compile(CharStream stream) {
        JPipeLexer lexer = new JPipeLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JPipeParser parser = new JPipeParser(tokens);
        ParseTree tree = parser.unit();
        System.out.println(tree.toStringTree(parser));
        return null;
    }



}
