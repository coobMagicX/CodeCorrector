public static boolean containsAny(CharSequence cs, int[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    for (int i = 0; i < csLength; i++) {
        int codePoint = Character.codePointAt(cs, i);
        for (int searchChar : searchChars) {
            if (searchChar == codePoint) {
                return true;
            }
        }
        if (Character.isSupplementaryCodePoint(codePoint)) {
            i++; // Skip the next character as it's a part of the surrogate pair
        }
    }
    return false;
}