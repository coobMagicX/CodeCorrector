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
  }

  if (newcode.contains("/")) {
    int index = newcode.indexOf("/");
    String regex = newcode.substring(index);
    append(regex);
  } else {
    append(newcode);
  }
}

static boolean isWordChar(char ch) {
  return (ch == '_' ||
          ch == '$' ||
          Character.isLetterOrDigit(ch));
}