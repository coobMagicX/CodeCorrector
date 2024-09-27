public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}

// Helper method to check if the CharSequence is empty
private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}

// Helper method to check if the array of characters is empty
private static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
}