public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (Character.isSurrogate(ch)) {
            if (Character.isHighSurrogate(ch)) {
                if (i + 1 < csLength) {
                    char ch2 = cs.charAt(i + 1);
                    if (Character.isLowSurrogate(ch2)) {
                        int codePoint = Character.toCodePoint(ch, ch2);
                        if (contains(searchChars, codePoint)) {
                            return true;
                        }
                        i++;
                    }
                }
            } else if (Character.isLowSurrogate(ch)) {
                if (i - 1 >= 0) {
                    char ch1 = cs.charAt(i - 1);
                    if (Character.isHighSurrogate(ch1)) {
                        // ch is a supplementary character
                        int codePoint = Character.toCodePoint(ch1, ch);
                        if (contains(searchChars, codePoint)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            if (contains(searchChars, ch)) {
                return true;
            }
        }
    }
    return false;
}

private static boolean contains(char[] array, int value) {
    for (char element : array) {
        if (element == value) {
            return true;
        }
    }
    return false;
}

private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}