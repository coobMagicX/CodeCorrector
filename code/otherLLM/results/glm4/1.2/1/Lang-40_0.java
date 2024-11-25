public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }

    // Convert both strings to the same locale for case-insensitive comparison.
   Locale defaultLocale = Locale.getDefault();
    String upperStr = str.toUpperCase(defaultLocale);
    String upperSearchStr = searchStr.toUpperCase(defaultLocale);

    return contains(upperStr, upperSearchStr);
}

public static boolean contains(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }

    int searchStrLen = searchStr.length();
    if (searchStrLen == 0) {
        return true;
    }
    
    // Use region-independent comparison for the actual string search.
    return str.indexOf(searchStr, 0) >= 0;
}