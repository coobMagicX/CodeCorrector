static boolean isSimpleNumber(String s) {
  int len = s.length();
  if (len == 1 && s.equals("0")) { // Special case for single digit numbers
    return true;
  }
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  return len > 0 && s.charAt(0) != '0';
}