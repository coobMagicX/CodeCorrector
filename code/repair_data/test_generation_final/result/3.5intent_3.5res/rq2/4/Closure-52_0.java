static boolean isSimpleNumber(String s) {
  int len = s.length();
  if (len > 1 && s.charAt(0) == '0') { // Check for octal representation
    for (int i = 1; i < len; i++) {
      char c = s.charAt(i);
      if (c < '0' || c > '7') {
        return false;
      }
    }
    return true;
  } else {
    for (int index = 0; index < len; index++) {
      char c = s.charAt(index);
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return len > 0;
  }
}