public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return str.toUpperCase().contains(searchStr.toUpperCase());
}