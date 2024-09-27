public static boolean isJSIdentifier(String s) {
    int length = s.length();

    if (length == 0 || !isJSIdentifierStart(s.charAt(0))) {
        return false;
    }

    for (int i = 1; i < length; i++) {
        if (!isJSIdentifierPart(s.charAt(i))) {
            return false;
        }
    }

    return true;
}

private static boolean isJSIdentifierStart(char ch) {
    return Character.isLetter(ch) || ch == '$' || ch == '_';
}

private static boolean isJSIdentifierPart(char ch) {
    return Character.isLetterOrDigit(ch) || ch == '$' || ch == '_';
}