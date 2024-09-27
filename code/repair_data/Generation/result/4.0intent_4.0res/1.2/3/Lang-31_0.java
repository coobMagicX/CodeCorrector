public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; ) {
        int codePoint = Character.codePointAt(cs, i);
        if (Character.isSupplementaryCodePoint(codePoint)) {
            if (contains(searchChars, Character.highSurrogate(codePoint)) && contains(searchChars, Character.lowSurrogate(codePoint))) {
                return true;
            }
            i += 2;
        } else {
            if (contains(searchChars, (char) codePoint)) {
                return true;
            }
            i++;
        }
    }
    return false;
}

private static boolean contains(char[] array, char value) {
    for (char element : array) {
        if (element == value) {
            return true;
        }
    }
    return false;
}