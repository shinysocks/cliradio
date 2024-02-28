public class ANSIColor {
    public final static boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows"); 
    public final static String DEFAULT = "\033[0m";
    public final static String BOLD_DEFAULT = "\033[37;1m";
    
    private final static String ERR = "\033[31;1m";
    private final static String BOLD_BLUE = "\033[34;1m";
    private final static String BOLD_GREEN = "\033[32;1m";
    private final static String BOLD_YELLOW = "\033[33;1m";
    private final static String BOLD_MAGENTA = "\033[35;1m";
    private final static String BOLD_CYAN = "\033[36;1m";
    private final static String BOLD_GRAY = "\033[90;1m";

    public final static String[] colors = new String[] {BOLD_BLUE, BOLD_CYAN, BOLD_MAGENTA, BOLD_YELLOW, BOLD_GREEN};

    public static String clientColor(int count) {
        return colors[count % colors.length];
    }

    public static String err(String text) {
        return ERR + text + DEFAULT;
    }

    public static String success(String text) {
        return BOLD_GREEN + text + DEFAULT;
    }
    
    public static String gray(String text) {
        return BOLD_GRAY + text + DEFAULT;
    }
    
    public static String magenta(String text) {
        return BOLD_MAGENTA + text + DEFAULT;
    }
    
}
