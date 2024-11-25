import java.text.Collator;

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }

    // Get the default locale and create a Collator for it.
    Collator collator = Collator.getInstance();
    
    // Set strength to handle accent sensitivity properly.
    collator.setStrength(Collator.PRIMARY);

    // Compare using the Collator, which will take into account locales and character encoding.
    return collator.compare(str.toLowerCase(), searchStr.toLowerCase()) == 0;
}