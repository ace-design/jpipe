package ca.mcscert.jpipe.actions.linking;

import ca.mcscert.jpipe.actions.Action;
import ca.mcscert.jpipe.actions.MacroAction;
import ca.mcscert.jpipe.error.UnknownSymbol;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.Pattern;
import java.util.List;
import java.util.function.Function;

/**
 * Implement a pattern into a given justification model
 */
public class ImplementsPattern extends MacroAction {

    private final String currentId;
    private final String parentId;

    public ImplementsPattern(String currentId, String parentId) {
        this.currentId = currentId;
        this.parentId = parentId;
    }

    @Override
    public Function<Unit, Boolean> condition() {
        return (u -> {
            try { u.get(currentId); } catch (UnknownSymbol us) { return false; }
            try { u.get(parentId);  } catch (UnknownSymbol us) { return false; }
            return (u.get(parentId) instanceof Pattern); // TODO should support extension and not just equality
        });
    }

    @Override
    public List<Action> expand(Unit u) throws Exception {
        return List.of();
    }

}
