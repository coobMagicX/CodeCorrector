public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !isJSIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    if (
        !isJSIdentifierPart(s.charAt(i))) {
      return false;
    }
  }

  return true;
}

public static boolean isJSIdentifierStart(char c) {
  return Character.isLetter(c) ||
         c == '_' ||
         c == '$';
}

public static boolean isJSIdentifierPart(char c) {
  return Character.isLetterOrDigit(c) ||
         c == '_' ||
         c == '$';
}