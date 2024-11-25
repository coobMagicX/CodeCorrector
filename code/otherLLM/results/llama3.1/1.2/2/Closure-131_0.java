public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isUnicodeIdentifierStart(s.codePointAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i += Character.charCount(s.codePointAt(i))) {
    if (
        !Character.isUnicodeIdentifierPart(s.codePointAt(i))) {
      return false;
    }
  }

  return true;
}