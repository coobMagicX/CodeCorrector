import java.text.Collator;
import java.util.Locale;

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Create a Collator for the default locale to handle locale-specific comparison.
    Collator collator = Collator.getInstance(Locale.getDefault());
    collator.setStrength(Collator.PRIMARY); // PRIMARY strength is case-insensitive
    
    return collator.compare(str, searchStr) == 0 || collator.compare(searchStr, str) == 0;
}