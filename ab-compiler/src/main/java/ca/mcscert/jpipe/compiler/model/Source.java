package ca.mcscert.jpipe.compiler.model;

import java.io.IOException;

public abstract class Source <I> {

    public abstract I provideFrom(String sourceName) throws IOException;

    public  <R> ChainBuilder<I,R> andThen(Transformation<I,R> next) {
        return new ChainBuilder<>(this, next);
    }

}
