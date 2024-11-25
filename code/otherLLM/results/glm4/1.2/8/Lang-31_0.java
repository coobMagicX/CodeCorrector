public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (Character.isSupplementaryCodePoint(ch)) {
            // Convert supplementary character to a code point array
            int[] codePoints = Character.toCodePointArray(ch);
            for (char searchChar : searchChars) {
                if (Character.isSupplementaryCodePoint(searchChar)) {
                    int[] searchCodePoints = Character.toCodePointArray(searchChar);
                    if (Arrays.equals(codePoints, searchCodePoints)) {
                        return true;
                    }
                } else if (searchChar == ch) {
                    return true;
                }
            }
        } else {
            for (char searchChar : searchChars) {
                if (searchChar == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}