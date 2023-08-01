package ca.mcscert.jpipe.model;

import ca.mcscert.jpipe.visitors.AbstractImpVisitor;
import ca.mcscert.jpipe.visitors.AbstractVisitor;

public class Implements  implements VisitableImp{

    private final String name;

    public Implements(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public void accept(AbstractImpVisitor<?> visitor) {
        visitor.visit(this);
    }
}
