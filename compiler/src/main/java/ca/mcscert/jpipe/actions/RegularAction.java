package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Unit;
import java.util.List;

/**
 * A regular action is an atomic action that can be executed over a justification unit.
 */
public abstract class RegularAction implements Action {

    @Override
    public final boolean isExpandable() {
        return false;
    }

    @Override
    public abstract void execute(Unit context) throws Exception;

    @Override
    public final List<Action> expand(Unit u) throws Exception {
        throw new UnsupportedOperationException("Regular action cannot be expanded");
    }
}
