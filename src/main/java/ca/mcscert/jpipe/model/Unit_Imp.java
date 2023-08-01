package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

import java.util.HashSet;
import java.util.Set;

public class Unit_Imp implements VisitableImp{

    private final String fileName;
    private final Set<Implementation> implementationSet;

    public Unit_Imp(String fileName) {
        this.fileName = fileName;
        this.implementationSet = new HashSet<>();
    }

    public void add(Implementation implementation) { this.implementationSet.add(implementation); }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }

    public Set<Implementation> getImplementationSet() {
        return implementationSet;
    }

    public String getFileName() {
        return fileName;
    }
}
