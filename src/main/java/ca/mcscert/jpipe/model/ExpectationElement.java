package ca.mcscert.jpipe.model;

public abstract class ExpectationElement {

    protected final String symbol;
    protected final String op;
    protected final int val;

    public ExpectationElement(String symbol, String op, int val)
    {
        this.symbol = symbol;
        this.op = op;
        this.val = val;
    }


    public int getVal() {
        return val;
    }

    public String getOp() {
        return op;
    }

    public String getSymbol() {
        return symbol;
    }
}
