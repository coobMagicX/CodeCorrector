static boolean isSimpleNumber(String s) {
  s = s.trim(); // Trim the input string to remove leading and trailing spaces
  s = s.replaceAll("\\s", ""); // Remove all spaces in the input string
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  return len > 0 && s.charAt(0) != '0';
}