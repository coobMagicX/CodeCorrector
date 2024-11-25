public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null || str.isEmpty() || searchStr.isEmpty()) {
        return false;
    }
    // Review case conversion logic and handle non-ASCII characters by using toUpperCase(Locale.ROOT)
    return contains(str.toUpperCase(Locale.ROOT), searchStr.toUpperCase(Locale.ROOT));
}