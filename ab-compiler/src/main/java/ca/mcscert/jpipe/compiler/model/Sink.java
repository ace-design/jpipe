package ca.mcscert.jpipe.compiler.model;

import java.io.IOException;

public interface Sink<O>  {

     void pourInto(O output, String fileName) throws IOException;

}

