public static boolean isJSIdentifier(String s) {
  if (s == null || s.isEmpty()) {
    return false;
  }

  // Check for JavaScript identifier including Unicode characters, $, and _
  // The identifier must start with a Unicode letter, $, or _, followed by any mix of Unicode letters,
  // digits, $, and _.
  // \p{L} and \p{Nd} are Unicode categories: L for letters and Nd for Decimal Digits.
  // These ensure broader language compatibility.
  String jsIdentifierRegex = "^[\\p{L}_$][\\p{L}\\p{Nd}_$]*$";

  return s.matches(jsIdentifierRegex);
}
