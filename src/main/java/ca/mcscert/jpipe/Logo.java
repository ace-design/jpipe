package ca.mcscert.jpipe;

/**
 * Pretty stupid utility class that prints the JPipe logo.
 */
public class Logo {

    @Override
    public String toString() {
        return
                "McMaster University - McMaster Centre for Software Certification (c) 2023-...\n"
                        + "   _ ______  _\n"
                        + "  (_)| ___ \\(_)\n"
                        + "   _ | |_/ / _  _ __    ___\n"
                        + "  | ||  __/ | || '_ \\  / _ \\\n"
                        + "  | || |    | || |_) ||  __/\n"
                        + "  | |\\_|    |_|| .__/  \\___|\n"
                        + " _/ |          | |\n"
                        + "|__/           |_|";
    }

    /**
     * print out the logo on stdout.
     */
    public static void sout() {
        System.out.println(new Logo());
    }

    /**
     * print out the logo on stderr (never used so far).
     */
    public static void serr() {
        System.err.println(new Logo());
    }



}
