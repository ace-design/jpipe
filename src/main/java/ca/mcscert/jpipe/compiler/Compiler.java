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
import java.nio.file.Path;

import java.util.HashSet;
import java.util.Set;


public class Compiler {

    private final Set<String> compiled = new HashSet<>();


    public Unit compile(String fileName) throws FileNotFoundException {
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            this.compiled.add(Paths.get(fileName).getFileName().toString());
            return this.compile(input, fileName);
        } catch (IOException e) {
            throw new FileNotFoundException(fileName);
        }
    }

    protected Boolean isCompiled(Path fileName){
        String file = fileName.getFileName().toString();
        if (compiled.contains(file)){
            return true;
        }
        return false;
    }

    public Unit compile(CharStream stream, String fileName) {
        JPipeLexer lexer = new JPipeLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JPipeParser parser = new JPipeParser(tokens);
        ParseTree tree = parser.unit();
        ModelCreationListener builder = new ModelCreationListener(fileName, this);
        ParseTreeWalker.DEFAULT.walk(builder, tree);
        return builder.build();
    }
}
