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

private static boolean isJSIdentifierStart(char c) {
    return Character.isLetter(c) || c == '_' || c == '$';
}

private static boolean isJSIdentifierPart(char c) {
    return Character.isLetterOrDigit(c) || c == '_' || c == '$';
}