package wothers.ift;

public final class NumberUtils {
    private NumberUtils() {}

    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static Integer tryParse(String s, Integer defaultValue) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
