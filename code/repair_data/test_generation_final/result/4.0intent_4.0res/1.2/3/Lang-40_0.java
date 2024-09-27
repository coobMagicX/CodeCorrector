import java.util.Locale;

public class StringUtils {

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        Locale locale = Locale.getDefault();
        return contains(str.toLowerCase(locale), searchStr.toLowerCase(locale));
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }
}