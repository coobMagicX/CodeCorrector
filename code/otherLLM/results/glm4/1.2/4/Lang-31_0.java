public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (Character.isSupplementaryCodePoint(ch)) {
            // ch is a supplementary character
            // We need to check if any of the search characters are the high surrogate part of a supplementary character
            for (int j = 0; j < searchLength; j++) {
                char searchChar = searchChars[j];
                if (Character.isHighSurrogate(searchChar) && Character.toLowerCase(ch) == Character.toLowerCase(searchChar)) {
                    // ch is in the Basic Multilingual Plane and matches a high surrogate
                    return true;
                }
            }
        } else if (searchChars[i] == ch) {
            // ch is not a supplementary character but is in the search array
            return true;
        }
    }
    return false;
}

private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}