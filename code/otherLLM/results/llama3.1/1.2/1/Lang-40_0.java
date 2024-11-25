public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    int start = str.indexOf(searchStr);
    for (int i = 0; i < searchStr.length(); i++) {
        char c1 = searchStr.charAt(i);
        for (int j = start; j <= str.length() - searchStr.length(); j++) {
            char c2 = str.charAt(j);
            if ((Character.toLowerCase(c1) == Character.toLowerCase(c2)) && (Character.toUpperCase(c1) != Character.toUpperCase(c2))) {
                return true;
            }
        }
    }
    return false;
}