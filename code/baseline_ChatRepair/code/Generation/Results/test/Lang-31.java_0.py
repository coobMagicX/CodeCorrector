public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    for (int i = 0; i < csLength; i++) {
        int cp = Character.codePointAt(cs, i);
        if (Character.charCount(cp) == 2) {
            i++; // Advance an extra step for surrogate pairs
        }
        for (char searchChar : searchChars) {
            if (cp == searchChar) {
                return true;
            }
        }
    }
    return false;
}
