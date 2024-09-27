public static boolean isJSIdentifier(String s) {
    int length = s.length();

    if (length == 0 ||
            !Character.isJavaIdentifierStart(s.charAt(0))) {
        return false;
    }

    for (int i = 1; i < length; i++) {
        char currentChar = s.charAt(i);
        if (!Character.isJavaIdentifierPart(currentChar) || Character.isISOControl(currentChar)) {
            return false;
        }
    }

    return true;
}