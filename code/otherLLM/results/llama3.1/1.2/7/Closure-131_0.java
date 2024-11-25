public static boolean isJSIdentifier(String s) {
  int length = s.length();

  if (length == 0 ||
      !Character.isJavaIdentifierStart(s.charAt(0))) {
    return false;
  }

  for (int i = 1; i < length; i++) {
    // Check for Unicode code points
    if (!isValidJavaScriptCodePoint(s.charAt(i))) {
      return false;
    }
  }

  return true;
}

public static boolean isValidJavaScriptCodePoint(char c) {
  String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$";
  return allowedChars.indexOf(c) != -1 ||
         Character.isLetterOrDigit(c) ||
         (c == '_' && s.charAt(i-1) != '_') ||
         (Character.isHighSurrogate(c)) ||
         (Character.isLowSurrogate(c));
}