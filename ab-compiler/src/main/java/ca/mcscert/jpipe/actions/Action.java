package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Unit;
import java.util.Map;

public interface Action {

    void execute(Unit context) throws Exception;

}
