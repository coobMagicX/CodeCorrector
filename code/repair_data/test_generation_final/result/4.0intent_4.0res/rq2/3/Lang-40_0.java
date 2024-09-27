import java.text.Collator;

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);  // Makes the comparison case insensitive
    return collator.compare(str, searchStr) == 0;
}