static boolean isSimpleNumber(String s) {
  if (s == null || s.isEmpty()) {
    return false;
  }
  
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  
  // The first character check should be adjusted to allow '0' as a valid number when it's the only character.
  return s.charAt(0) != '0' || len == 1;
}