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
    
    // Check if the last character was a forward slash
    char lastChar = getLastChar();
    if (lastChar == '/') {
      // If it was, append another forward slash instead of a space
      append("/");
    }
  }

  append(newcode);
}