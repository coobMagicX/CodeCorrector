import java.text.Collator;
import java.util.Locale;

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    Collator collator = Collator.getInstance(Locale.getDefault());
    collator.setStrength(Collator.PRIMARY); // Ignore case and accents

    return contains(str, collator, searchStr);
}

private static boolean contains(String str, Collator collator, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Normalize both strings for locale-specific comparisons
    str = collator.normalize(str);
    searchStr = collator.normalize(searchStr);

    int index = 0;
    while (index < str.length()) {
        // Find the index of the next occurrence of searchStr in str
        int foundIndex = str.indexOf(searchStr, index);

        if (foundIndex != -1) {
            return true;
        }

        index = foundIndex + 1;
    }
    return false;
}