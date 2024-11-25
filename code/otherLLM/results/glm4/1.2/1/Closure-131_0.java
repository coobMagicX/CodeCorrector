public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isJavaIdentifierStart(s.charAt(0)) ||
      Character.toString(s.charAt(0)).equals("\u0004")) { // Check for non-breaking space at the start
    return false;
  }

  for (int i = 1; i < length; i++) {
    if (
        !Character.isJavaIdentifierPart(s.charAt(i)) ||
        Character.toString(s.charAt(i)).equals("\u0004")) { // Check for non-breaking space in the middle
      return false;
    }
  }

  return true;
}