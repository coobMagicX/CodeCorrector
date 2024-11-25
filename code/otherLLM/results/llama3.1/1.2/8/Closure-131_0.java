public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Check for quoted identifiers (enhance Unicode support)
  if (s.startsWith("\"") || s.startsWith("\'")) {
    String trimmedS = s.substring(1, length - 1); // Remove quotes
    return Character.isJavaLetter(trimmedS.charAt(0)) && 
           !Character.isSpaceChar(trimmedS.charAt(0)) &&
           isJSIdentifier(trimmedS);
  }

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

  return true;
}