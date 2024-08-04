package ca.mcscert.jpipe.actions;

import ca.mcscert.jpipe.model.Unit;

/**
 * Define what an Action is. (Actions implement a command pattern for model creation)
 */
public interface Action {

    /**
     * An action is executed on a Unit, which is modified as a result by its side effect.
     *
     * @param context the unit to apply the action to.
     * @throws Exception is something goes wrong.
     */
    void execute(Unit context) throws Exception;

}
