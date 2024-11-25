public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        // Check if the character is a supplementary character and adjust the index accordingly
        if (Character.isSupplementaryCodePoint(ch)) {
            ch = Character.toLowerCase(Character.highSurrogate(ch));
        }
        
        for (int j = 0; j < searchLength; j++) {
            char searchChar = searchChars[j];
            // Check if the search character is a supplementary character and adjust the index accordingly
            if (Character.isSupplementaryCodePoint(searchChar)) {
                searchChar = Character.toLowerCase(Character.highSurrogate(searchChar));
            }
            
            if (ch == searchChar) {
                return true;
            }
        }
    }
    return false;
}