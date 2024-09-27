public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (Character.isHighSurrogate(ch) && i + 1 < csLength) {
            char chLow = cs.charAt(i + 1);
            if (Character.isLowSurrogate(chLow)) {
                int supplementaryCodePoint = Character.toCodePoint(ch, chLow);
                for (int j = 0; j < searchLength; j++) {
                    if (Character.isSupplementaryCodePoint(searchChars[j]) && searchChars[j] == supplementaryCodePoint) {
                        return true;
                    }
                }
                i++; // Move past the low surrogate as well
            }
        } else {
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}