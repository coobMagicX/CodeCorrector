public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }

    int csLength = cs.length();
    for (int i = 0; i < csLength; ) {
        int csCodePoint = Character.codePointAt(cs, i);
        int csCharCount = Character.charCount(csCodePoint);
        i += csCharCount; 

        for (int j = 0; j < searchChars.length; ) {
            int searchCharCodePoint = Character.codePointAt(searchChars, j);
            int searchCharCount = Character.charCount(searchCharCodePoint);
            j += searchCharCount; 

            if (csCode