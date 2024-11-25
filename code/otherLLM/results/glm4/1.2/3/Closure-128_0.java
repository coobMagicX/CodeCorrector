static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (!Character.isDigit(c)) { // Changed from the previous condition to use Character.isDigit
      return false;
    }
  }
  return len > 0 && s.charAt(0) != '0';
}