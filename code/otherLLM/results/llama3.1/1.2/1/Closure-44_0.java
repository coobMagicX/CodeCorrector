void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    // need space to separate. This is not pretty printing.
    // For example: "return foo;"
    append(" ");
    
    // Fix edge case handling: Allow a forward slash to appear after a DIV
    if (getLastChar() == '/') {
      append("/");
    } else {
      append(newcode);
      return;
    }
  }

  append(newcode);
}