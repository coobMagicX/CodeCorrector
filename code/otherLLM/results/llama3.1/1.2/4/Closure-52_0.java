static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if ((c == '0' && index != 0 && s.startsWith("0x", 0)) || // Handle hexadecimal prefixes
        (c < '0' || c > '9') && !Character.isDigit(c) && // Allow decimal numbers and hexadecimal digits
        !(Character.toUpperCase(c) >= 'A' && Character.toUpperCase(c) <= 'F')) {
      return false;
    }
  }
  return len > 0;
}