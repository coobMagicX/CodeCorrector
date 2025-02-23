public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || searchChars == null || searchChars.length == 0) {
        return false;
    }
    
    
    Set<Integer> searchCharSet = new HashSet<>();
    for (int i = 0; i < searchChars.length; ) {
        int cp = Character.codePointAt(searchChars, i);
        searchCharSet.add(cp);
        i += Character.charCount(cp);  
    }

    
    final int csLength = cs.length