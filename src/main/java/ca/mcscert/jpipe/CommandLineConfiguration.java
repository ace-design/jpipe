package ca.mcscert.jpipe;

import org.apache.commons.cli.*;

import java.util.Optional;

public class CommandLineConfiguration {

    private static final String APP_NAME = "java -jar jpipe.jar";

    private final String[] userGiven;

    public CommandLineConfiguration(String[] userGiven) {
        this.userGiven = userGiven;
    }

    public Optional<CommandLine> read() throws ParseException {
        CommandLineParser parser = new DefaultParser();

        if (checkForHelp(userGiven)) {
            return Optional.empty();
        }
        Options options = getOptions();
        CommandLine cmd = parser.parse(options, userGiven);
        System.out.println(cmd);
        return Optional.of(cmd);
    }

    public void help() {
        Logo.sout();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, getOptions());
    }


    private boolean checkForHelp(String[] args) throws ParseException {
        Options help = new Options();
        help.addOption(new Option("h", "help", false, "display help message"));
        CommandLineParser parser = new DefaultParser();
        CommandLine tmp = parser.parse(help,args, true);
        return tmp.hasOption("h");
    }

    private Options getOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false, "display help message");
        options.addOption(help);

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(true);
        options.addOption(output);

        Option diagram = new Option("d", "diagram", true, "diagram name");
        diagram.setRequired(true);
        options.addOption(diagram);

        return options;
    }






}
