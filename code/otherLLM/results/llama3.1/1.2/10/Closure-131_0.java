public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Check for quoted property names separately, considering their specific rules and character sets.
  if (length >= 2 && s.charAt(0) == '"' && s.charAt(length - 1) == '"') {
    String value = s.substring(1, length - 1);
    int valueLength = value.length();
    
    // Check for Unicode code points in the quoted property name
    boolean hasUnicodeCodePoint = false;
    for (int i = 0; i < valueLength; i++) {
      if ((value.charAt(i) & 0x80) != 0) { // Check for Unicode code point using 0x80 mask
        hasUnicodeCodePoint = true;
        break;
      }
    }

    // Check for non-ASCII characters in the quoted property name
    boolean hasNonAsciiChar = false;
    for (int i = 0; i < valueLength; i++) {
      if ((value.charAt(i) & 0x80) != 0 && value.charAt(i) != '"') { // Check for non-ASCII character excluding double quotes
        hasNonAsciiChar = true;
        break;
      }
    }

    // If quoted property name contains Unicode code points or non-ASCII characters, it's a valid JS identifier
    if (hasUnicodeCodePoint || hasNonAsciiChar) {
      return true;
    }
  } else {
    // Check for Java identifiers separately
    if (length == 0 ||
        !Character.isJavaIdentifierStart(s.charAt(0))) {
      return false;
    }

    for (int i = 1; i < length; i++) {
      if (
          !Character.isJavaIdentifierPart(s.charAt(i))) {
        return false;
      }
    }
    
    // If Java identifier doesn't contain Unicode code points, it's a valid JS identifier
    boolean hasUnicodeCodePoint = false;
    for (int i = 0; i < length; i++) {
      if ((s.charAt(i) & 0x80) != 0) { // Check for Unicode code point using 0x80 mask
        hasUnicodeCodePoint = true;
        break;
      }
    }

    return !hasUnicodeCodePoint;
  }
}