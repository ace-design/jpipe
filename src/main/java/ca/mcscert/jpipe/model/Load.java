package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.compiler.Compiler;
import ca.mcscert.jpipe.visitors.AbstractImpVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Load implements VisitableImp{

    private final String fileName;

    public Load(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
