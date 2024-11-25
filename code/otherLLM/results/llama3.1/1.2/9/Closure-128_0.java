static boolean isSimpleNumber(String s) {
  int len = s.length();
  if (len == 0) { // Handle empty string edge case
    return false;
  }
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') {
      return false;
    }
  }
  return true; // Corrected condition to allow leading zeros
}