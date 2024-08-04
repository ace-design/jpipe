package ca.mcscert.jpipe.compiler.model;

import ca.mcscert.jpipe.compiler.Compiler;
import java.io.IOException;

public class ChainCompiler<Input,Output> implements Compiler {

    private final Source<Input> inputSource;
    private final Transformation<Input, Output> chain;
    private final Sink<Output> outputSink;

    public ChainCompiler(Source<Input> inputSource, Transformation<Input, Output> chain, Sink<Output> outputSink) {
        this.inputSource = inputSource;
        this.chain = chain;
        this.outputSink = outputSink;
    }

    @Override
    public void compile(String sourceFile, String sinkFile) throws IOException {
        Input i = inputSource.provideFrom(sourceFile);
        Output o = chain.fire(i, sourceFile);
        outputSink.pourInto(o, sinkFile);
    }

}
