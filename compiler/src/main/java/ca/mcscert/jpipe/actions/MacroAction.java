package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Unit;
import java.util.List;

public abstract class MacroAction implements Action {

    @Override
    public final boolean isExpandable() {
        return true;
    }

    @Override
    public final void execute(Unit context) throws Exception {
        throw new UnsupportedOperationException("Macro-action cannot be executed");
    }

    @Override
    public abstract List<Action> expand(Unit u) throws Exception;
}
