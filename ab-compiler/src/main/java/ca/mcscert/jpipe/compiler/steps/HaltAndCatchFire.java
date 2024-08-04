package ca.mcscert.jpipe.compiler.steps;

import ca.mcscert.jpipe.compiler.model.Transformation;
import ca.mcscert.jpipe.error.ErrorManager;
import ca.mcscert.jpipe.error.FatalException;
import java.util.List;

public final class HaltAndCatchFire<T> extends Transformation<T,T> {

    private final List<Throwable> errors;

    public HaltAndCatchFire(List<Throwable> errors) {
        this.errors = errors;
    }

    @Override
    protected T run(T input, String source) throws Exception {
        if (this.errors.isEmpty()) {
            return input;
        } else {
            errors.forEach(ErrorManager.getInstance()::registerError);
            throw new FatalException("Compilation aborted");
        }
    }
}
