package ca.mcscert.jpipe.compiler.visitors;

import ca.mcscert.jpipe.model.*;
import ca.mcscert.jpipe.visitors.DefaultVisitor;

public class EvidenceCounter extends DefaultVisitor<Integer> {


    public EvidenceCounter() {
        this.result = 0;
    }
    @Override
    public void visit(Evidence e) {
        result++;
    }


}
