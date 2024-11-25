static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c == '0' && index == 0 && (len > 1 || !Character.isDigit(s.charAt(1)))) { // Handle leading zeros correctly
      return false;
    }
    if (c == 'x' && index == 0 && len == 2) { // Recognize hexadecimal prefix
      continue;
    }
    if (c == '.' && index == 0) { // Recognize decimal point at the beginning
      return isSimpleNumber(s.substring(1));
    }
    if (c < '0' || c > '9') {
      return false;
    }
  }
  return true; // Return true when the string length is greater than 0, considering all valid numeric keys
}