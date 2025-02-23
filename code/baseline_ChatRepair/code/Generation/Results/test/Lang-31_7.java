public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }
    
    
    String searchStr = new String(searchChars);
    
    int csLength = cs.length();
    for (int i = 0; i < csLength; i++) {
        int cp = Character.codePointAt(cs, i);
        if (Character.isSupplementaryCodePoint(cp)) {
            i++; 
        }
        
        
        for (int j = 0;