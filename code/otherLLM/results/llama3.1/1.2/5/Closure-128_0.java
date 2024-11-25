static boolean isSimpleNumber(String s) {
  if (s == null || s.isEmpty()) { // added input validation
    return false;
  }

  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  // added handling for zero-padding
  String trimmedS = s.replaceAll("^0+", "");
  return !trimmedS.isEmpty() && trimmedS.charAt(0) != '0';
}