public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isUnicodeIdentifierStart(s.charAt(0)) || // Update to use Unicode identifier start method
      (s.charAt(0) == '_' && length == 1)) { // Handle edge case for underscore followed by nothing
    return false;
  }

  for (int i = 1; i < length; i++) {
    if (
        !Character.isUnicodeIdentifierPart(s.charAt(i))) { // Update to use Unicode identifier part method
      return false;
    }
  }

  return true;
}