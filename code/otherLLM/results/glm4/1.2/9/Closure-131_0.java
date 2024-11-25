public static boolean isJSIdentifier(String s) {
    int length = s.length();

    // Check for null or empty string
    if (s == null || length == 0) {
        return false;
    }

    // Check the first character
    if (!Character.isJavaIdentifierStart(s.charAt(0))) {
        return false;
    }

    // Iterate over the rest of the characters
    for (int i = 1; i < length; i++) {
        char ch = s.charAt(i);

        // Check for valid identifier part excluding control characters and unicode escapes
        if (!Character.isJavaIdentifierPart(ch) || Character.isISOControl(ch)) {
            return false;
        }
    }

    return true;
}