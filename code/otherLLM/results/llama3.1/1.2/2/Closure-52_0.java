static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c == '0' && (index == 0 || s.charAt(index - 1) == 'x') && (index + 1 < len && s.charAt(index + 1) == 'x')) {
      return false;
    }
    if (c < '0' || c > '9' || (c == '.' && index != 0)) {
      return false;
    }
  }
  return true; // no need to check len, the loop will run at least once
}