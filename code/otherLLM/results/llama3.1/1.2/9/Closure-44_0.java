void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    append(" ");
    // Do not allow a forward slash to appear after a DIV.
    // For example,
    // REGEXP DIV REGEXP
    // is valid and should print like
    // / // / /
    if (c == '/') {
      int count = 0;
      for (int i = 1; i < newcode.length(); i++) {
        if (newcode.charAt(i) == '/') {
          append("/ ");
          count++;
        } else {
          break;
        }
      }
      // If there's only one forward slash, print it without a space
      if (count == 0) {
        append("/");
      }
    }
  }

  append(newcode);
}