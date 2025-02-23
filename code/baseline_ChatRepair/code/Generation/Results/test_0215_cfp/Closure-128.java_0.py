static boolean isSimpleNumber(String s) {
  int len = s.length();
  if (len == 0) {
    return false;
  }

  if (s.equals("0")) { // Directly handle the case of the string "0"
    return true; 
  }

  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
    if (index == 0 && c == '0') { // Ensure no leading zero unless it's "0"
      return false;
    }
  }

  return true;
}
