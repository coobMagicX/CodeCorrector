public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Check for empty string or invalid start character
  if (length == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  // Iterate over each character in the string starting from the second character
  for (int i = 1; i < length; i++) {
    char ch = s.charAt(i);
    // Check for non-ASCII control characters that are valid JavaScript identifiers
    if (!Character.isJavaIdentifierPart(ch) && (ch >= 0x00 && ch <= 0x1F)) {
      return false;
    }
  }

  return true;
}