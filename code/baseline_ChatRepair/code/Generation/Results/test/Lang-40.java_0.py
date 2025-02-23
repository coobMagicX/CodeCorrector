public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return contains(str.toUpperCase(Locale.ROOT), searchStr.toUpperCase(Locale.ROOT));
}
