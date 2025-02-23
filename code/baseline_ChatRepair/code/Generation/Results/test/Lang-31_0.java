public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    
    String searchCharsString = new String(searchChars);

    
    int csLength = cs.length();
    for (int i = 0; i < csLength; ) {
        int cp = Character.codePointAt(cs, i);
        int charCount = Character.charCount(cp);
        
        
        if (searchCharsString.indexOf(cp) >= 0) {
            return true;
        }

        