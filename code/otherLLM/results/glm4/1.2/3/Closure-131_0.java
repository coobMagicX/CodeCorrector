public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Check for empty string or invalid first character
  if (length == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    char ch = s.charAt(i);

    // Check for valid JavaScript identifier part
    if (!Character.isJavaIdentifierPart(ch)) {
      return false;
    }
    
    // Handling replacement character and other non-standard Unicode characters
    if (ch == '\u0004' || Character.isISOControl(ch) && !Character.isSurrogate(ch)) {
      // The method is not supposed to accept control characters other than surrogates
      return false;
    }
  }

  return true;
}