public static boolean isJSIdentifier(String s) {
  if (s == null || s.isEmpty()) {
    return false;
  }

  int length = s.length();
  char firstChar = s.charAt(0);
  
  
  if (!isValidJSIdentifierStart(firstChar)) {
    return false;
  }

