public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }
    
    
    String searchCharsString = new String(searchChars);

    int csLength = cs.length();
    int searchLength = searchCharsString.length();
    
    for (int i = 0; i < csLength;) {
        int csCodePoint = Character.codePointAt(cs, i);
        i += Character.charCount(csCodePoint);

        for (int j = 0; j < searchLength;) {
            int searchCodePoint = Character.codePointAt(searchCharsString, j);
            j += Character.charCount(searchCodePoint);
            
            if (csCodePoint == searchCodePoint) {
                return true;
           