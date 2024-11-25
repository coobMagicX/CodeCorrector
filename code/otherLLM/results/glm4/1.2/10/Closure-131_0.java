public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    // Check if the character is a Java Identifier Part or not
    if (!Character.isJavaIdentifierPart(s.charAt(i)) &&
        // Additionally, ensure it is not a Unicode character that is not allowed in JavaScript identifiers
        !isAllowedUnicodeCharacter(s.charAt(i))) {
      return false;
    }
  }

  return true;
}

// Helper method to check if the Unicode character is allowed in JavaScript identifiers
private static boolean isAllowedUnicodeCharacter(char c) {
  // Check for allowed ASCII characters and extended ASCII range
  if (c >= 0 && c <= 127) return true;

  // Add any other ranges or specific characters that are allowed here

  return false; // Default to not allowed if not in an allowed range
}