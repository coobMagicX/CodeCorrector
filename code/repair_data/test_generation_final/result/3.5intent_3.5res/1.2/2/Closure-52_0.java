static boolean isNumeric(String s) {
  return s.matches("0[xX][0-9a-fA-F]+|0+[0-7]*|[1-9][0-9]*|0*\\.\\d+");
}