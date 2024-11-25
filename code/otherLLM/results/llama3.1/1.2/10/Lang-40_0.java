public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return equals(toUpperCase(str), toUpperCase(searchStr));
}

private static String toUpperCase(String str) {
    return str != null ? str.toUpperCase() : null;
}