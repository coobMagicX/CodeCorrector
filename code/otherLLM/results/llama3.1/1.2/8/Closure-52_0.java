static boolean isSimpleNumber(String s) {
  int len = s.length();
  // Check for hexadecimal prefix '0x'
  if (s.startsWith("0x")) {
    // Remove the prefix and check the rest of the string
    return isSimpleNumber(s.substring(2));
  }
  
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      // If the character is not a digit, check for decimal numbers
      if (s.startsWith(".") && index == 1) {
        // If it's a decimal number with leading dot, return false
        return false;
      } else {
        return false;
      }
    }
  }
  return len > 0;
}