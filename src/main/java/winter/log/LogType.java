package winter.log;

public enum LogType {
    INFO("\u001B[32m", "\u001B[42m"),
    WARNING("\u001B[33m", "\u001B[43m"),
    ERROR("\u001B[31m", "\u001B[41m"),
    ;

    private final String color;
    private final String background;

    LogType(String color, String background) {
        this.color = color;
        this.background = background;
    }

    public String getColor() {
        return color;
    }

    public String getBackground() {
        return background;
    }
}
