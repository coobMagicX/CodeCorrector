public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return contains(str.toUpperCase(), searchStr.toUpperCase());
}

public static boolean contains(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return str.indexOf(searchStr) >= 0;
}