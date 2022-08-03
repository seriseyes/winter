package winter.log;

/**
 * Log
 *
 * @author Баярхүү.Лув, 2022.08.03
 */
public class Log {
    public static final String ANSI_RESET = "\u001B[0m";

    public static void println(String message, LogType logType) {
        System.out.println(logType.getColor() + logType.name() + ": " + ANSI_RESET + message);
    }
}
