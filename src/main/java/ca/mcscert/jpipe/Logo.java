package ca.mcscert.jpipe;

public class Logo {

    @Override
    public String toString() {
        return
                "McMaster University - McMaster Centre for Software Certification (c) 2023-...\n" +
                "   _ ______  _\n" +
                "  (_)| ___ \\(_)\n" +
                "   _ | |_/ / _  _ __    ___\n" +
                "  | ||  __/ | || '_ \\  / _ \\\n" +
                "  | || |    | || |_) ||  __/\n" +
                "  | |\\_|    |_|| .__/  \\___|\n" +
                " _/ |          | |\n" +
                "|__/           |_|";
    }

    public static void sout() {
        System.out.println(new Logo());
    }

    public static void serr() {
        System.err.println(new Logo());
    }



}
