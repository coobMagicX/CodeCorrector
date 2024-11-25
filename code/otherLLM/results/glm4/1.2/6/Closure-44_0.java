void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char prevChar = getLastChar();
  for (int i = 0; i < newcode.length(); i++) {
    char c = newcode.charAt(i);
    if ((isWordChar(c) || c == '\\') && (i == 0 || isWordChar(prevChar))) {
      // Need space to separate. This is not pretty printing.
      // For example: "return foo;"
      append(" ");
      // Do not allow a forward slash to appear after a DIV.
      if (c == '/' && i > 0 && newcode.charAt(i - 1) == 'D' && newcode.substring(0, i - 1).contains("DIV")) {
        continue;
      }
    }
    append(String.valueOf(c));
    prevChar = c;
  }
}