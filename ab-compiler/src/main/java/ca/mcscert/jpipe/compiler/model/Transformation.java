package ca.mcscert.jpipe.compiler.model;


import ca.mcscert.jpipe.error.ErrorManager;
import ca.mcscert.jpipe.error.FatalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Transformation<I,O> {

    protected static final Logger logger = LogManager.getLogger();

    protected abstract O run(I input, String source) throws Exception;

    public final O fire(I in, String source) {
        logger.info("Firing pass [{}]", this.getClass().getCanonicalName());
        O out = null;
        try {
            out = run(in, source);
            return out;
        } catch (FatalException fe) {
            throw fe; // propagate the exception to stop the process
        } catch (Exception e) {
            // Something unexpected went wrong. Register and abort;
            ErrorManager.getInstance().fatal(e);
            return null;
        }
    }

    public  <R> Transformation<I,R> andThen(Transformation<O,R> next) {
        Transformation<I,O> myself = this;
        return new Transformation<>() {
            @Override
            protected R run(I input, String source) throws Exception {
                O temporary = myself.run(input, source);
                return next.fire(temporary, source);
            }
        };
    }

}


