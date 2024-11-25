public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    // Convert both strings to lower case using the locale's case folding rules
    int searchStart = 0;
    while (searchStart < str.length() && Character.foldCase(str.charAt(searchStart)) != Character.foldCase(searchStr.charAt(0))) {
        searchStart++;
    }
    return contains(str.substring(searchStart), searchStr.toLowerCase());
}