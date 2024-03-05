public class TUI {
    public final static boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static String logo() {
        return Color.DEFAULT + ":------------:\n" +
                "|  " + Color.BOLD_BLUE + "cliradio" + Color.DEFAULT + "  |\n" +
                ":------------:" + Color.DEFAULT;
    }

    public static String hostChatStart() {
        return Color.gray(
                "   _____________________________________________________________\n Ctrl+c to quit, select microphone or internal capture in settings");
    }

    public static String clientChatStart() {
        return Color.gray(
                "   ____________________________________________________\n Ctrl+c to quit, select speaker or headphones in settings");
    }

    public static class Color {
        public final static String DEFAULT = IS_WINDOWS ? "": "\u001b[0m";
        private final static String BOLD_RED = IS_WINDOWS ? "": "\u001b[31;1m";
        private final static String BOLD_BLUE = IS_WINDOWS ? "": "\u001b[34;1m";
        private final static String BOLD_GREEN = IS_WINDOWS ? "": "\u001b[32;1m";
        private final static String BOLD_YELLOW = IS_WINDOWS ? "": "\u001b[33;1m";
        private final static String BOLD_MAGENTA = IS_WINDOWS ? "": "\u001b[35;1m";
        private final static String BOLD_CYAN = IS_WINDOWS ? "": "\u001b[36;1m";
        private final static String BOLD_GRAY = IS_WINDOWS ? "": "\u001b[90;1m";

        public final static String[] colors = new String[] { BOLD_BLUE, BOLD_CYAN, BOLD_MAGENTA, BOLD_YELLOW,
                BOLD_GREEN };

        public static String clientColor(int count) {
            return colors[count % colors.length];
        }

        public static String err(String text) {
            return BOLD_RED + text + DEFAULT;
        }

        public static String warn(String text) {
            return BOLD_YELLOW + text + DEFAULT;
        }

        public static String success(String text) {
            return BOLD_GREEN + text + DEFAULT;
        }

        public static String gray(String text) {
            return BOLD_GRAY + text + DEFAULT;
        }

        public static String blue(String text) {
            return BOLD_BLUE + text + DEFAULT;
        }
    }
}
