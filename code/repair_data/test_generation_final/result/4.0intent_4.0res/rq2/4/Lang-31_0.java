public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        if (Character.isHighSurrogate(cs.charAt(i)) && i + 1 < csLength && Character.isLowSurrogate(cs.charAt(i + 1))) {
            int csCodePoint = Character.toCodePoint(cs.charAt(i), cs.charAt(i + 1));
            for (int j = 0; j < searchLength; j++) {
                if (j + 1 < searchLength && Character.isSurrogatePair(searchChars[j], searchChars[j + 1])) {
                    int searchCodePoint = Character.toCodePoint(searchChars[j], searchChars[j + 1]);
                    if (searchCodePoint == csCodePoint) {
                        return true;
                    }
                    j++; // Move past the low surrogate
                } else if (searchChars[j] == cs.charAt(i)) {
                    return true;
                }
            }
            i++; // Move past the low surrogate in the input sequence
        } else {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}