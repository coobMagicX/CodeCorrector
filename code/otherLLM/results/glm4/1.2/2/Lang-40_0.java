public static boolean containsIgnoreCase(String str, String searchStr, Locale locale) {
    if (str == null || searchStr == null) {
        return false;
    }
    if (locale == null) {
        locale = Locale.getDefault(); // Fallback to default locale if locale is not provided
    }
    return contains(str, searchStr, locale);
}

private static boolean contains(String str, String searchStr, Locale locale) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Use the case-insensitive comparison with the specified locale
    String lowerStr = str.toLowerCase(locale);
    String lowerSearchStr = searchStr.toLowerCase(locale);

    return lowerStr.contains(lowerSearchStr);
}