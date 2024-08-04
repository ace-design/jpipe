package ca.mcscert.jpipe.compiler.model;

import ca.mcscert.jpipe.compiler.Compiler;

public final class ChainBuilder<I,O> {

    private final Source<I> source;
    private final Transformation<I,O> chain;

    public ChainBuilder(Source<I> source, Transformation<I,O> next) {
        this.chain = next;
        this.source = source;
    }

    public <R> ChainBuilder<I,R> andThen(Transformation<O,R> next) {
        return new ChainBuilder<>(this.source, chain.andThen(next));
    }

    public ChainCompiler<I,O> andThen(Sink<O> sink) {
        return new ChainCompiler<>(this.source, this.chain, sink);
    }



}
