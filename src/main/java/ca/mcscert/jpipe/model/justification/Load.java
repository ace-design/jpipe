package ca.mcscert.jpipe.model.justification;

import ca.mcscert.jpipe.model.Visitable;
import ca.mcscert.jpipe.visitors.AbstractVisitor;
import java.nio.file.Path;

/**
 * Load directive (equivalent to a #include for the compiler).
 */
public class Load implements Visitable {

    private final String path;

    public Load(Path loadFilePath, Path parentFilePath) {
        this.path = generatePath(parentFilePath, loadFilePath);
    }

    private String generatePath(Path parent, Path child) {
        return parent.getParent().toString() + "/" + child.toString();
    }

    public String getLoadPath() {
        return this.path;
    }

    @Override
    public void accept(AbstractVisitor<?> visitor) {
        visitor.visit(this);
    }

    
}
