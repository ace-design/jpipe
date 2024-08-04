package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.actions.Action;
import ca.mcscert.jpipe.actions.ExecutionEngine;
import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.model.Unit;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ActionListInterpretation extends Transformation<List<Action>, Unit> {

    private static final Logger logger = LogManager.getLogger();

    @Override
    protected Unit run(List<Action> input, String source) throws Exception {
        ExecutionEngine engine = new ExecutionEngine();
        return engine.spawn(source, input);
    }

}
