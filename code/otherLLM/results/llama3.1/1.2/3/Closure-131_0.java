public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  // Check for Unicode escape sequences or surrogate pairs
  int codePointCount = countCodePoints(s);
  for (int i = 1; i < length; i += Character.charCount(s.charAt(i))) {
    if (!Character.isJavaIdentifierPart(s.codePointAt(i))) {
      return false;
    }
  }

  // Check the last character separately
  int codePointIndex = s.offsetByCodePoints(0, -1);
  if (codePointIndex < length && !Character.isJavaIdentifierPart(s.codePointAt(codePointIndex))) {
    return false;
  }

  return true;
}

// Helper method to count Unicode code points in the string
public static int countCodePoints(String s) {
  int codePointCount = 0;
  for (int i = 0; i < s.length(); i += Character.charCount(s.charAt(i))) {
    codePointCount++;
  }
  return codePointCount;
}