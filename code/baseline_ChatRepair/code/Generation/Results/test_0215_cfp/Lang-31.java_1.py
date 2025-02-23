public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }
    int csLength = cs.length();
    for (int i = 0; i < csLength; i++) {
        int cp = Character.codePointAt(cs, i);
        if (Character.charCount(cp) == 2) { // character is a supplementary character
            i++; // Skip next char as it is a part of the surrogate pair
        }

        // Iterate every code point in searchChars (considering possible surrogate pairs)
        for (int j = 0; j < searchChars.length; j++) {
            int searchChar = Character.codePointAt(searchChars, j);
            if (Character.charCount(searchChar) == 2) {
                j++; // Skip next char as it is a part of the surrogate pair in searchChars
            }
            if (cp == searchChar) {
                return true;
            }
        }
    }
    return false;
}
