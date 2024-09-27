public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        for (int j = 0; j < searchLength; j++) {
            if (Character.isHighSurrogate(searchChars[j]) && i + 1 < csLength && Character.isLowSurrogate(cs.charAt(i + 1))) {
                if (searchChars[j] == cs.charAt(i) && searchChars[j + 1] == cs.charAt(i + 1)) {
                    return true;
                }
            } else if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}