package ca.mcscert.jpipe.compiler;

import java.io.IOException;

public interface Compiler {

    void compile(String sourceFile, String sinkFile) throws IOException;

}
