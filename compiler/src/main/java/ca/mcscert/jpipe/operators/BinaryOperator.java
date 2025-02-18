package ca.mcscert.jpipe.operators;


public interface BinaryOperator<L, R, O> {

    O apply(L left, R right);

}
