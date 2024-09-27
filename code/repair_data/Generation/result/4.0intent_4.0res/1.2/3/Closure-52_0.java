static boolean isSimpleNumber(String s) {
  int len = s.length();
  if (len == 0) {
    return false;
  }

  if (s.startsWith("0x")) {
    // Check if the string represents a hexadecimal number
    String hexNumber = s.substring(2);
    try {
      Integer.parseInt(hexNumber, 16);
      return false; // Hexadecimal numbers are not simple numbers
    } catch (NumberFormatException e) {
      // The string is not a valid hexadecimal number
    }
  }

  if (s.contains(".")) {
    // Check if the string represents a floating-point number
    try {
      Float.parseFloat(s);
      return false; // Floating-point numbers are not simple numbers
    } catch (NumberFormatException e) {
      // The string is not a valid floating-point number
    }
  }

  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  
  // Check if the string is a valid octal number
  if (len > 1 && s.charAt(0) == '0') {
    try {
      Integer.parseInt(s, 8);
      return false; // Octal numbers are not simple numbers
    } catch (NumberFormatException e) {
      // The string is not a valid octal number
    }
  }
  
  return true;
}