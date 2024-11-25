static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c == '0') { // Reject leading zeros
      return false;
    }
    if (c < '0' || c > '9') { // Check for valid digits
      if (c == '.' && (index == 1 || s.startsWith("0"))) { // Allow decimal points in numbers
        continue;
      }
      if (Character.isDigit(c)) {
        continue;
      }
      return false; // Reject hexadecimal numbers and special characters
    }
  }
  return len > 0;
}