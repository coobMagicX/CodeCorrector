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

  append(newcode);
}

static boolean isWordChar(char ch) {
  return (ch == '_' ||
          ch == '$' ||
          Character.isLetterOrDigit(ch));
}

void maybeEndStatement() {
  if (getLastChar() == ';' || getLastChar() == '}') {
    append("\n");
  }
}

char getLastChar() {
  if (length() == 0) {
    return 0;
  }
  return charAt(length() - 1);
}

void append(String code) {
  // Append the code to the existing source code
}