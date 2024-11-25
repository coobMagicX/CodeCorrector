static boolean isSimpleNumber(String s) {
  int len = s.length();
  // Check for a leading zero only if the string length is greater than 1.
  if (len > 1 && s.charAt(0) == '0') {
    char secondChar = s.charAt(1);
    // If it's a decimal number, we expect at least one digit after the decimal point,
    // or an exponentiation sign followed by digits.
    if (secondChar == '.') {
      boolean hasDecimalPoint = false;
      for (int index = 2; index < len; index++) {
        char c = s.charAt(index);
        if (!Character.isDigit(c) && c != 'e' && c != 'E') {
          return false;
        }
        if (!hasDecimalPoint && c == '.') {
          hasDecimalPoint = true;
        }
      }
    } else if (secondChar >= '0' && secondChar <= '9') {
      // It's an octal number, check if it can be parsed as an octal.
      try {
        Integer.parseInt(s, 8);
      } catch (NumberFormatException e) {
        return false;
      }
    } else {
      return false;
    }
  }
  
  // For numbers without a leading zero or with a '0' followed by another digit,
  // we check if the string is numeric.
  for (int index = len > 1 ? 1 : 0; index < len; index++) {
    char c = s.charAt(index);
    if (!Character.isDigit(c) && c != '.') {
      return false;
    }
  }
  
  return true;
}