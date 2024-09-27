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
    // Do not allow a forward slash to appear after a DIV.
    // For example,
    // REGEXP DIV REGEXP
    // is valid and should print like
    // / // / /

    // Check if the current slash is part of a regex expression
    boolean isRegexDelimiter = false;
    if (c == '/') {
      int endIndex = newcode.length() - 1;
      while (endIndex >= 0 && newcode.charAt(endIndex) == '\\') {
        endIndex--;
      }
      isRegexDelimiter = (endIndex >= 0 && newcode.charAt(endIndex) == '/');
    }

    if (isRegexDelimiter && getLastChar() == '/') {
      append(" ");
    }
  }

  // Check if the current code is a line comment
  if (newcode.startsWith("//")) {
    // If the previous code ends with a line comment, add a space before the comment
    if (getLastChar() == '/') {
      append(" ");
    }
  }

  append(newcode);
}