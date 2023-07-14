package ca.mcscert.jpipe.compiler.visitors;

import ca.mcscert.jpipe.model.Conclusion;
import ca.mcscert.jpipe.model.Evidence;
import ca.mcscert.jpipe.visitors.DefaultVisitor;

import java.util.HashMap;
import java.util.Map;

public class ElementCounter extends DefaultVisitor<Map<String, Integer>> {

    public static final String EVIDENCE = "evidence";
    public static final String CONCLUSION = "conclusion";


    public ElementCounter() {
        this.result = new HashMap<>();
        result.put(EVIDENCE, 0);
        result.put(CONCLUSION, 0);
    }
    @Override
    public void visit(Evidence e) {
        super.visit(e);
        increment(EVIDENCE);
    }

    @Override
    public void visit(Conclusion c) {
        super.visit(c);
        increment(CONCLUSION);
    }

    private void increment (String symbol) {
        int old = result.getOrDefault(symbol,0);
        result.put(symbol, old + 1);
    }

}
