package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;

import java.util.HashSet;
import java.util.Set;

public class Implementation implements VisitableImp {
    private final String imp_name;
    private final String justification_name;
    private final Set<Implements> implementsSet;
    public Implementation(String imp_name, String justification_name, Set<Implements> implementsSet)
    {
        this.imp_name = imp_name;
        this.justification_name = justification_name;
        this.implementsSet = new HashSet<>();
    }

    public void add(Implements anImplements) { this.implementsSet.add(anImplements); }

    public String getJustification_name() {
        return justification_name;
    }

    public String getImp_name() {
        return imp_name;
    }

    public Set<Implements> getImplementsSet() {
        return implementsSet;
    }

    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
