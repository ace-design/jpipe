package ca.mcscert.jpipe;

/**
 * Pretty stupid utility class that prints the JPipe logo.
 *
 * @author mosser
 */
public final class Logo {

    @Override
    public String toString() {
        return """
               McMaster University - McSCert (c) 2023-...
                    _  ____  _  ____  _____
                   / |/  __\\/ \\/  __\\/  __/
                   | ||  \\/|| ||  \\/||  \\
                /\\_| ||  __/| ||  __/|  /_
                \\____/\\_/   \\_/\\_/   \\____\\
               """;
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
