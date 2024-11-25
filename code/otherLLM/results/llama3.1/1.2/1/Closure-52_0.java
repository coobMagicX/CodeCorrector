static boolean isSimpleNumber(String s) {
  int len = s.length();
  // Remove leading zeros
  while (len > 1 && s.charAt(0) == '0') {
    s = s.substring(1);
    len--;
  }
  
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      // Check for numeric prefixes
      String prefix = "";
      if (s.startsWith("0x") || s.startsWith("0b")) {
        prefix = s.substring(0, 2);
        s = s.substring(2);
        len -= 2;
      }
      if (c < '0' || c > '9') {
        return false;
      } else if (s.contains(".")) {
        String[] parts = s.split("\\.");
        // Check for decimal numbers
        if (parts.length == 1 && parts[0].equals("0")) {
          return false;
        }
        int decimalLen = parts[1].length();
        for (int i = 0; i < decimalLen; i++) {
          char d = parts[1].charAt(i);
          if (d < '0' || d > '9') {
            return false;
          }
        }
      } else {
        // Check for non-numeric characters in the remaining string
        for (int i = index + 1; i < len; i++) {
          char c2 = s.charAt(i);
          if (c2 < '0' || c2 > '9') {
            return false;
          }
        }
      }
    }
  }
  return true;
}