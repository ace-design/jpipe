package ca.mcscert.jpipe.actions.creation;

import ca.mcscert.jpipe.actions.RegularAction;
import ca.mcscert.jpipe.error.UnknownSymbol;
import ca.mcscert.jpipe.model.Unit;
import ca.mcscert.jpipe.model.elements.JustificationModel;
import java.util.List;
import java.util.function.Function;

/**
 * Lock a justification model, indicating to other that it is now complete and cannot be
 * modified anymore.
 */
public class LockJustificationModel extends RegularAction {

    private final String modelId;
    private final List<String> dependencies;


    public LockJustificationModel(String modelId) {
        this(modelId, List.of());
    }

    public LockJustificationModel(String modelId, List<String> dependencies) {
        this.modelId = modelId;
        this.dependencies = dependencies;
    }

    @Override
    public void execute(Unit context) throws Exception {
        // By design, will only be executed when dependencies are locked
        context.get(modelId).lock();
    }

    @Override
    public Function<Unit, Boolean> condition() {
        return (u -> {
            for (String dep : dependencies) {
                try {
                    JustificationModel model = u.get(dep);
                    if (!model.isLocked()) {
                        return false;
                    }
                } catch (UnknownSymbol us) { return false; }
            }
            return true;
        });
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LockJustificationModel{");
        sb.append("modelId='").append(modelId).append('\'');
        sb.append(", dependencies=").append(dependencies);
        sb.append('}');
        return sb.toString();
    }
}
