static boolean isSimpleNumber(String s) {
  int len = s.length();

  // Regular expressions for different numeric formats
  String octalRegex = "0[0-7]+";
  String hexadecimalRegex = "0x[0-9a-fA-F]+";
  String decimalRegex = "\\d+(\\.\\d+)?";

  // Check if the string matches any of the formats
  if (s.matches(octalRegex) || s.matches(hexadecimalRegex) || s.matches(decimalRegex)) {
    return true;
  } else {
    return false;
  }
}