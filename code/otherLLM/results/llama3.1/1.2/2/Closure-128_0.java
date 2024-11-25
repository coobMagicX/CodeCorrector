static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  // Fix: allow leading zeros
  return len == 1 && s.charAt(0) != '0' || s.startsWith("0") != true;
}