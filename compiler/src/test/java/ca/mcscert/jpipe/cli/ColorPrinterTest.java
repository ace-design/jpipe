package ca.mcscert.jpipe.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPrinterTest {

    private ByteArrayOutputStream outContent;
    private ColorPrinter colorPrinter;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        colorPrinter = new ColorPrinter(outContent);
    }

    @AfterEach
    void tearDown() {
        outContent = null;
        colorPrinter = null;
    }

    @Test
    void testPrintln() {
        String message = "Hello";
        colorPrinter.println(message);
        String expected = ANSI_RED + message + ANSI_RESET + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testPrint() {
        String message = "World";
        colorPrinter.print(message);
        String expected = ANSI_RED + message + ANSI_RESET;
        assertEquals(expected, outContent.toString());
    }
}
