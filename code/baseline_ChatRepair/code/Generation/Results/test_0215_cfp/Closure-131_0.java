public static boolean isJSIdentifier(String s) {
    if (s == null || s.isEmpty()) {
        return false;
    }

    
    if (!Character.isUnicodeIdentifierStart(s.charAt(0)) && s.charAt(0) != '$' && s.charAt(0) != '_') {
        return false;
    }

    
    for (int