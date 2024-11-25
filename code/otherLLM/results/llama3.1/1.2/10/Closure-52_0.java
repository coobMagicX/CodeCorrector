static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c == '0' && (index != 0 || !isHexPrefix(s))) { // Handle leading zeros in non-hexadecimal numbers
      return false;
    }
    if ((c < '0' || c > '9') && c != '.' && (c != 'x' || s.length() <= index + 2)) { // Account for decimal points and hexadecimal prefixes
      return false;
    }
  }
  return len > 0;
}

static boolean isHexPrefix(String s) {
  if (s.length() < 2) return false; // No hex prefix in strings shorter than 2 characters
  String prefix = s.substring(0, 2);
  return (prefix.equals("0x") || prefix.equals("-0x")); // Check for "0x" or "-0x"
}