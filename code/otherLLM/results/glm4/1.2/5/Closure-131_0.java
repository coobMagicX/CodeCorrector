public static boolean isJSIdentifier(String s) {
    int length = s.length();

    if (length == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
        return false;
    }

    for (int i = 1; i < length; i++) {
        // Check if the character is a Java Identifier Part or one of the JavaScript-specific allowed characters
        char ch = s.charAt(i);
        if (!Character.isJavaIdentifierPart(ch) && 
            !isJavaScriptSpecificIdentifierChar(ch)) {
            return false;
        }
    }

    return true;
}

// Helper method to check for JavaScript-specific identifier characters
private static boolean isJavaScriptSpecificIdentifierChar(char ch) {
    // Unicode ranges for JavaScript specific Identifier characters (excluding the ASCII range)
    if ((ch >= 0x80 && ch <= 0xFF) || // Extended ASCII
        (ch >= 0x100 && ch <= 0x1FF) || // ISO 8859-1
        (ch >= 0x200 && ch <= 0xD7FF) || // Basic Multilingual Plane
        (ch >= 0xE000 && ch <= 0xFFFF)) { // Private Use Area
            return true;
    }
    return false;
}