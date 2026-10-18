package dr.web;

/** Replacements for System.getProperty / System.exit (see tools/src/Rewrite.java). */
public final class Sys {
    /** Must match the manifest's "Nokia-Platform: *" pattern, which rejects an empty string. */
    private static final String PLATFORM = "NokiaE71-1/300.21.012";

    private static String locale;

    private Sys() {
    }

    public static String getProperty(String key) {
        switch (key) {
            case "microedition.platform":
                return PLATFORM;
            case "microedition.locale":
                return locale();
            case "microedition.configuration":
                return "CLDC-1.1";
            case "microedition.profiles":
                return "MIDP-2.0";
            case "microedition.encoding":
                return "ISO-8859-1";
            default:
                return null;
        }
    }

    public static void exit(int status) {
        Host.exited("System.exit(" + status + ")");
        throw new ExitError();
    }

    /**
     * Picks a language the JAR ships (lang.es-US, lang.fr, ...) from the browser's preferences.
     * Anything else returns "en", for which the game falls back to lang.xx (English).
     */
    private static String locale() {
        if (locale != null) {
            return locale;
        }
        locale = "en";
        for (String wanted : Host.languages().split(",")) {
            wanted = wanted.trim();
            if (wanted.isEmpty()) {
                continue;
            }
            if (Res.has("lang." + wanted)) {
                locale = wanted;
                return locale;
            }
            String language = wanted.split("-")[0].toLowerCase();
            if (language.equals("en")) {
                return locale;
            }
            for (String name : Res.names()) {
                if (name.startsWith("lang.") && !name.equals("lang.xx")
                        && name.substring(5).split("-")[0].toLowerCase().equals(language)) {
                    locale = name.substring(5);
                    return locale;
                }
            }
        }
        return locale;
    }

    public static final class ExitError extends Error {
        ExitError() {
            super("MIDlet called System.exit");
        }
    }
}
