public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isUnicodeIdentifierStart(s, 0)) { // Update check to allow for Unicode code points not allowed by Java's Character class
    return false;
  }

  for (int i = 1; i < length; i++) {
    if (
        !Character.isUnicodeIdentifierPart(s, i)) { // Relax restrictions on certain Unicode code points, such as those in the IDS Start category
      return false;
    }
  }

  return true;
}