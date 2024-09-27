static boolean isSimpleNumber(String s) {
  if (s == null || s.isEmpty()) {
    return false;
  }
  int len = s.length();
  if (s.charAt(0) == '0' && len > 1) {
    return false;
  }
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  return true;
}