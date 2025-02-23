public static boolean isJSIdentifier(String s) {
  if (s == null || s.isEmpty()) return false;
  
  // This regex pattern ensures that:
  // - the string must start with a Unicode letter, `$`, or `_`
  // - the subsequent characters can be Unicode letters, `$`, `_`, or digits
  // Expressions \p{L} and \p{N} target Unicode letters and digits respectively
  String identifierRegex = "^[$\\p{L}_][\\d\\p{L}_]*$";

  return s.matches(identifierRegex);
}
