public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isLetter(s.charAt(0)) && s.charAt(0) != '_') { // Check for underscore as a valid start character
    return false;
  }

  for (int i = 1; i < length; i++) {
    char c = s.charAt(i);
    if (
        !Character.isJavaIdentifierPart(c) || // Use the existing method to check identifier part
        (c == '.' && i != length - 1) ||      // Check for dots not followed by a number or letter
        (i > length - 3 && c == '.')          // Check for consecutive dots at the end of the string
    ) {
      return false;
    }
  }

  return true;
}