static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if ((c != '-' && c < '0' || c > '9') && !Character.isDigit(c)) { // Modified condition to include special characters and non-digit characters
      return false;
    }
  }
  return len > 0 && (s.charAt(0) == '-' ? s.length() >= 2 : true); // Corrected the condition for negative numbers
}