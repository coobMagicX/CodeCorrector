public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }

    // Get the default locale
    Locale locale = Locale.getDefault();

    // Convert the strings to lowercase using the locale
    String strLowerCase = str.toLowerCase(locale);
    String searchStrLowerCase = searchStr.toLowerCase(locale);

    // Pass the lowercase strings to the contains method
    return contains(strLowerCase, searchStrLowerCase);
}

public static boolean contains(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return str.indexOf(searchStr) >= 0;
}