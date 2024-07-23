package ca.mcscert.jpipe.compiler;

import ca.mcscert.jpipe.ColorPrinter;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Log all errors encountered while compiling the unit.
 */
public class CompilationErrorLog {

    private static CompilationErrorLog singleton;

    /**
     * Offer a global compilationError log using hte singleton pattern.
     *
     * @return a singleton-ed instance of the log.
     */
    public static CompilationErrorLog singleton() {
        if (singleton == null) {
            singleton = new CompilationErrorLog();
        }
        return singleton;
    }

    private CompilationErrorLog() { }

    /**
     * Represent a compilation error that happened during the process.
     *
     * @param line the line where the error was detected (null if NA).
     * @param column the column where the error was detected (null if NA)
     * @param source the source of the error (ce.g., compilation phase)
     * @param message the error message
     */
    private record Log(Integer line, Integer column, String source, String message) {

        public String asText() {
            return (this.source() == null ? "" : this.source())
                    + (this.line() == null ? "" : ":" + line)
                    + (this.column() == null ? "" : ":" + this.column())
                    + " - " + message + System.lineSeparator();
        }

        /**
         * Transform a log into a JSON object.
         *
         * @return a JSON representation of the error log (used by LSP/IDE).
         */
        public JSONObject asJson() {
            JSONObject obj = new JSONObject();
            obj.put("message", message());
            if (source() != null) {
                obj.put("source", source());
            }
            if (column() != null) {
                obj.put("column", column());
            }
            if (line() != null) {
                obj.put("line", line());
            }
            return obj;
        }
    }

    private final List<Log> contents = new LinkedList<>();

    /**
     * Record a message into the log.
     *
     * @param message the message to record.
     */
    public void record(String message) {
        this.record(null, null, null, message);
    }

    /**
     * Record a message into the log, from a given source.
     *
     * @param source the source of the error.
     * @param message the message to record.
     */
    public void record(String source, String message) {
        this.record(null, null, source, message);
    }

    /**
     * Record a message into the log, from a given source, at a given location.
     *
     * @param line the line where the error was detected (null if NA).
     * @param column the column where the error was detected (null if NA)
     * @param source the source of the error (ce.g., compilation phase)
     * @param message the error message
     */
    public void record(Integer line, Integer column, String source, String message) {
        this.contents.add(new Log(line, column, source, message));
    }

    /**
     * Print the contents of the error log as text message on the console.
     *
     * @param stream the stream to be used for printing.
     */
    public void printAsText(PrintStream stream) {
        ColorPrinter printer = new ColorPrinter(stream);
        for (Log log : contents) {
            printer.println(log.asText());
        }
    }

    /**
     * Print the content of the error log as JSON elements.
     *
     * @param stream the stream to be used for printing.
     */
    public void printAsJson(PrintStream stream) {
        JSONArray json = new JSONArray();
        for (Log log : contents) {
           json.put(log.asJson());
        }
        stream.println(json);
    }
}
