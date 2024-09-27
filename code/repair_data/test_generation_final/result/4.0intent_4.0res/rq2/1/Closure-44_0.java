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
  }

  // Check if the new code starts with a '/' and if the last character is also '/'
  // to avoid ending up with "//" which could be mistaken as the start of a comment
  if (c == '/' && getLastChar() == '/') {
    append(" ");
  }

  append(newcode);
}