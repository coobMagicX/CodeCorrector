static boolean isSimpleNumber(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if ((c == '0' && (index > 0 || !isOctal(s))) || 
        (c == 'x' && isHexadecimal(s)) || 
        (c == '.' && hasDecimalPoint(s))) {
      return false;
    }
  }
  return len > 0;
}

private boolean isOctal(String s) {
  int len = s.length();
  for (int index = 1; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c >= '8') {
      return false;
    }
  }
  return true;
}

private boolean isHexadecimal(String s) {
  int len = s.length();
  for (int index = 2; index < len; index++) {
    char c = s.charAt(index);
    if ((c < '0' || c > '9') && 
        !(c >= 'a' && c <= 'f') && 
        !(c >= 'A' && c <= 'F')) {
      return false;
    }
  }
  return true;
}

private boolean hasDecimalPoint(String s) {
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c == '.') {
      return true;
    }
  }
  return false;
}