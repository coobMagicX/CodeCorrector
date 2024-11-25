public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        // Check if 'ch' is a supplementary character
        if (Character.isSurrogate(ch)) {
            // If it's a surrogate pair, get the actual code point using Character.toCodePoint
            int codePoint = Character.toCodePoint(ch, cs.charAt(i + 1));
            ch = (char) codePoint;
            i++; // Skip the next character since we've already handled it as part of the surrogate pair
        }
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}

// Note: This code assumes that the method ArrayUtils.isEmpty is defined elsewhere in your library.