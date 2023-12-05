package ca.mcscert.jpipe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class controls how the command line arguments are processed to configure the compiler.
 */
public class CommandLineConfiguration {

    private static final String APP_NAME = "java -jar jpipe.jar";
    private final String[] userGiven;

    /**
     * Constructor for the command line configuration.
     *
     * @param userGiven the argument string provided by the user when invoking the compiler
     */
    public CommandLineConfiguration(String[] userGiven) {
        this.userGiven = userGiven;
    }

    /**
     * Parse the command line to extract configuration option asked by the user.
     *
     * @return Empty if the user asks for help (whatever the other options are),
     *         the parsed options elsewhere.
     * @throws ParseException if the arguments provided by the user does not match the CLI usage
     */
    public Optional<CommandLine> read() throws ParseException {
        CommandLineParser parser = new DefaultParser();

        if (checkForHelp(userGiven)) {
            return Optional.empty();
        }

        Options options = getOptions();
        CommandLine cmd = parser.parse(options, userGiven);
        return Optional.of(cmd);
    }

    /**
     * Print the help message explaining to the user how to use the command line.
     */
    public void help() {
        Logo.sout();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, getOptions(), true);
    }

    /**
     * Check in the provided arguments if the user invoked the "help" command (-h, --help).
     *
     * @param args the arguments provided by the user
     * @return true if the option is found
     * @throws ParseException is Apache CLI cannot parse the arguments
     */
    private boolean checkForHelp(String[] args) throws ParseException {
        Set<String> arguments = new HashSet<>(Arrays.asList(args));
        return (arguments.contains("-h") || arguments.contains("--help"));
    }

    /**
     * Configure the command line parser to know what to look for.
     *
     * @return an Options object used by Apache CLI to parse the provided arguments
     */
    private Options getOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false,
                "display help message");
        options.addOption(help);

        Option input = new Option("i", "input", true,
                "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true,
                "output file(s) directory\n(default: .)");
        output.setRequired(false);
        options.addOption(output);


        Option diagram = new Option("d", "diagram", true,
                "diagram names\n(use multiple -d if needed. Default: all)");
        diagram.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(diagram);

        Option format = new Option("f", "format", true,
                "output format in [png, svg]\n(Default: png)");
        format.setRequired(false);
        options.addOption(format);

        Option logLvl = new Option(null, "log-level", true,
                "log level for Java logging API\n(default: ERROR)");
        format.setRequired(false);
        options.addOption(logLvl);


        return options;
    }
}

