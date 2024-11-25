public static boolean isJSIdentifier(String s) {
    if (s == null || s.isEmpty()) {
        return false;
    }

    // Check if the first character is an ASCII identifier start.
    char firstChar = s.charAt(0);
    if (!Character.isJavaIdentifierStart(firstChar)) {
        return false;
    }

    for (int i = 1; i < s.length(); i++) {
        char ch = s.charAt(i);

        // Check if the character is a valid part of an identifier.
        if (!Character.isJavaIdentifierPart(ch) && ch != '$') {
            return false;
        }
    }

    return true;
}