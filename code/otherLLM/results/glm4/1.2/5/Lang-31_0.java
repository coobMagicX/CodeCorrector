public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        // Check for supplementary characters
        if (Character.isSurrogate(ch)) {
            // Extract the actual code point from surrogate pair
            int codePoint = Character.toCodePoint(ch, cs.charAt(++i));
            // Check for the presence of this code point in the searchChars array
            if (containsAny(cs, String.valueOf(codePoint).toCharArray())) {
                return true;
            }
        } else {
            // ch is not a supplementary character, so just check if it's in the Basic Multilingual Plane
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}

// Helper method used to determine if a CharSequence is empty, similar to the one in ArrayUtils
private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}