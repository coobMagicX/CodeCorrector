public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return containsCaseInsensitive(str, searchStr);
}

private static boolean containsCaseInsensitive(String str, String searchStr) {
    int i = 0;
    int j = 0;
    while (i < str.length() && j < searchStr.length()) {
        char c1 = str.charAt(i);
        char c2 = searchStr.charAt(j);
        if (c1 == c2 || Character.toLowerCase(c1) == Character.toLowerCase(c2)) {
            i++;
            j++;
        } else {
            i++; // ignore non-matching characters
        }
    }
    return j >= searchStr.length();
}