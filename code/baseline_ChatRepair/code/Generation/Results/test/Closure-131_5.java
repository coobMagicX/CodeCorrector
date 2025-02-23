public static boolean isJSIdentifier(String s) {
    int length = s.length();

    if (length == 0) {
        return false;
    }
    
    char firstChar = s.charAt(0);
    if (!Character.isLetter(firstChar) && firstChar != '$' && firstChar != '_') {
        return false;
    }

    for (int i = 1