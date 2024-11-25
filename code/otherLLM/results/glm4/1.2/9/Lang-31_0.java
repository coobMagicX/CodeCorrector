public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        // Check if the character is supplementary and in BMP
        if (Character.isSupplementaryCodePoint(ch)) {
            ch = Character.toSurrogateCodePoint((char)(ch >> 16), (char)((ch & 0xFFFF) - 0x10000));
        }
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}

public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}