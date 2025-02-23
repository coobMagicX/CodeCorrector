public static boolean isJSIdentifier(String s) {
  if (s == null || s.isEmpty()) {
    return false;
  }

  
  
  char firstChar = s.charAt(0);
  if (!isValidJSIdentifierStart(firstChar)) {
    return false;
