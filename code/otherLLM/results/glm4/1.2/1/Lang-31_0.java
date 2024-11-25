public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        // Check if 'ch' is a supplementary character and handle it properly
        if (Character.isSupplementaryCodePoint(ch)) {
            // Convert to the actual code point of the supplementary character
            ch = Character.codePointAt(cs, i);
            i++; // Skip the next char because we've already considered it as part of a supplementary character
        }
        for (int j = 0; j < searchLength; j++) {
            char searchChar = searchChars[j];
            if (Character.isSupplementaryCodePoint(searchChar)) {
                // Convert to the actual code point of the supplementary character from searchChars array
                searchChar = Character.codePointAt(searchChars, j);
                j++; // Skip the next char because we've already considered it as part of a supplementary character
            }
            if (ch == searchChar) {
                return true;
            }
        }
    }
    return false;
}

// Helper method to check for empty CharSequence (assuming this is not implemented elsewhere)
private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}