public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }

    
    String searchStr = new String(searchChars);

    
    int csLength = cs.length();
    for (int i = 0; i < csLength; ) {
        int cp = Character.codePointAt(cs, i);
        int charCount = Character.charCount(cp);

        
        if (searchStr.indexOf(cp) >= 0) {
            return true;
        }

        