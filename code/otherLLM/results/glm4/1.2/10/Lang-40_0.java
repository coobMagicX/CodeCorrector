import java.util.Locale;

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Using toUpperCase with a Locale for internationalized comparison
    return str.toUpperCase(Locale.ROOT).contains(searchStr.toUpperCase(Locale.ROOT));
}