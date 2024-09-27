public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    Locale currentLocale = Locale.getDefault();
    return contains(str.toLowerCase(currentLocale), searchStr.toLowerCase(currentLocale));
}