public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Ensure the string is not null and starts with a valid identifier start character.
  if (length == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    char ch = s.charAt(i);

    // Check that the current character is not a control character and is a valid identifier part.
    if (!Character.isJavaIdentifierPart(ch)) {
      // Check for non-ASCII characters by ensuring they are not control characters
      // or surrogates (which are invalid in identifiers).
      if (!Character.isISOControl(ch) && !Character.isSurrogate(ch)) {
        return false;
      }
    }
  }

  return true;
}