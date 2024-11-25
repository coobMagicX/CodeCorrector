public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    char c = s.charAt(i);
    if (!Character.isJavaIdentifierPart(c)) {
      // Check for Unicode code points
      int codePoint = Character.toCodePoint(s, i);
      if ((codePoint >= 0x10FFFF || (codePoint & 0xFFFF) == 0) && 
          !isUnicodeIdentifierChar(codePoint)) {
        return false;
      }
    }
  }

  return true;
}

public static boolean isUnicodeIdentifierChar(int codePoint) {
  // Based on the JavaScript identifier syntax, this function should check
  // if a Unicode code point is allowed in an identifier.
  // For example:
  return (codePoint >= 0x4E00 && codePoint <= 0x9FFF) ||
         (codePoint == 0xA67A) ||
         (codePoint == 0xFE22);
}