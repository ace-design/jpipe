package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.error.ErrorManager;
import ca.mcscert.jpipe.error.FatalException;
import ca.mcscert.jpipe.model.Unit;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An execution engine to apply any necessary action to a given compilation unit.
 */
public final class ExecutionEngine {

    private static final Logger logger = LogManager.getLogger();
    private final boolean failOnError;

    /**
     * Create an execution engine, which does not fail when encountering an error (default).
     */
    public ExecutionEngine() {
        this.failOnError = false;
    }

    /**
     * Create an execution engine.
     *
     * @param failOnError set to True to change default behavior when encountering error failure.
     */
    public ExecutionEngine(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
     * Spawn a new compilation Unit from scratch.
     * We create a brand-new unit and then apply provided actions on it.
     *
     * @param fileName the source file associated to that unit.
     * @param actions the actions to apply to the empty Unit.
     * @return the Unit after all the actions have been applied.
     */
    public Unit spawn(String fileName, List<Action> actions) {
        Unit unit = new Unit(fileName);
        this.update(unit, actions);
        return unit;
    }

    private void update(Unit u, List<Action> actions) {
        List<Throwable> errors = new ArrayList<>();
        this.execute(actions, u, errors);
        if (!errors.isEmpty()) {
            errors.forEach(ErrorManager.getInstance()::registerError);
            if (failOnError) {
                throw new FatalException("Error while executing actions");
            }
        }
    }

    private void execute(List<Action> actions, Unit u, List<Throwable> errors)  {
        if (actions.isEmpty()) {
            return;
        }
        Action a = actions.removeFirst();
        logger.trace("Executing action [{}]", a);
        try {
            a.execute(u);
        } catch (Exception e) {
            logger.info("  Error while executing action [{}]", e.getMessage());
            logger.trace(u);
            errors.add(e);
        }
        this.execute(actions, u, errors);
    }
}
