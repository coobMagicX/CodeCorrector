public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Use the toLowerCase() method from String class which handles locale-specific case conversion.
    return str.toLowerCase().contains(searchStr.toLowerCase());
}