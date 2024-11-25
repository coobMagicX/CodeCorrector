public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    // Use locale-specific case folding to handle locale-sensitive character mappings.
    return str.toLowerCase().equals(searchStr.toLowerCase()) || contains(str.toUpperCase(), searchStr.toUpperCase());
}