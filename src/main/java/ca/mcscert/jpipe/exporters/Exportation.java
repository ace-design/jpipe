package ca.mcscert.jpipe.exporters;

/**
 * Generic interface to export "things" into files.
 *
 * @param <T> which kind of element are we exporting.
 */
public interface Exportation<T> {

    /**
     * Export element into outputFile.
     *
     * @param element the element to export (e.g., a justification diagram)
     * @param outputFile the file where to put the result of the exportation.
     * @param format file format to export to.
     */
    void export(T element, String outputFile, String format);

}
