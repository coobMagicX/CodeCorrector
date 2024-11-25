public static boolean isJSIdentifier(String s) {
  int length = s.length();

  // Check if the string is empty or starts with a non-identifier character
  if (length == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    char c = s.charAt(i);
    
    // Check if the character is a valid identifier part or one of the allowed special characters
    if (!Character.isJavaIdentifierPart(c) && !(c == '_' || c == '$')) {
      return false;
    }
  }

  return true;
}