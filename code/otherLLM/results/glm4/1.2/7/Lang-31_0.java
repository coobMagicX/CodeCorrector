public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                // Check if the character is not a supplementary character by ensuring it's within the BMP range.
                if (ch <= 0xFFFF) {
                    return true;
                }
                // If the character is a supplementary character, we need to check if it exists in the `cs` CharSequence.
                else {
                    // Create a String from the CharSequence to properly handle supplementary characters.
                    String csString = cs.toString();
                    int index = csString.indexOf(searchChars[j]);
                    if (index != -1) {
                        return true;
                    }
                }
            }
        }
    }
    return false;
}