void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  char lastChar = getLastChar(); // Get the last character before appending.

  if ((isWordChar(c) || c == '\\') &&
      isWordChar(lastChar)) {
    if (c != '/' && !breakAfterBlockFor(null, false)) { // Adjust condition to avoid adding space after a DIV.
      append(" ");
    }
  }

  append(newcode);
}