package ca.mcscert.jpipe.exporters;

public interface Exportation<T> {

    void export(T element, String outputFile);


}
