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
  char lastChar = getLastChar();
  if (lastChar != ';' && lastChar != '{' && lastChar != '}') {
    append(";");
  }
}

char getLastChar() {
  if (code.length() == 0) {
    return '\0';
  }
  return code.charAt(code.length() - 1);
}

void append(String str) {
  code += str;
}