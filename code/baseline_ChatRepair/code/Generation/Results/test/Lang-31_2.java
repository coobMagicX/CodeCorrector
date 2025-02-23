public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }

    for (int i = 0; i < cs.length(); i = cs.offsetByCodePoints(i, 1)) {
        int cp = Character.codePointAt(cs, i);

        for (int j = 0; j < searchChars.length; j = j + Character.charCount(searchChar)) {
            int searchChar = Character.codePointAt(searchChars, j);
            if (cp == searchChar) {
                return true;
            }
        }
    }
    return false;
}

private static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0