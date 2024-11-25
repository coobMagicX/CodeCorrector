public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (char c : searchChars) {
            if (c == ch) {
                // Check if the character is in the Basic Multilingual Plane (BMP)
                int codePoint = Character.codePointAt(cs, i);
                return Character.toCodePoint(c) <= 0xFFFF;
            }
        }
    }
    return false;
}