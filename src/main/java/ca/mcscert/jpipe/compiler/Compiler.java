package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.syntax.JPipeLexer;
import ca.mcscert.jpipe.syntax.JPipeParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;


public class Compiler {


    public Unit compile(String fileName) throws FileNotFoundException {
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            return this.compile(input, fileName);
        } catch (IOException e) {
            throw new FileNotFoundException(fileName);
        }
    }

    public Unit compile(CharStream stream, String fileName) {
        JPipeLexer lexer = new JPipeLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JPipeParser parser = new JPipeParser(tokens);
        ParseTree tree = parser.unit();
        ModelCreationListener builder = new ModelCreationListener(fileName);
        ParseTreeWalker.DEFAULT.walk(builder, tree);
        return builder.build();
    }
}
