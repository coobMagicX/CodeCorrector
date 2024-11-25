void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);

  // Check for the need to add a space before characters like 'c' and '\'
  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    append(" ");
  }

  // Do not allow a forward slash to appear after a DIV
  int i = newcode.indexOf('/');
  if (i != -1 && (newcode.length() > i + 1 && newcode.charAt(i + 1) == '/')) {
    append("/ "); // Add space before and avoid consecutive slashes
  } else {
    append(newcode); // Otherwise, append the new code as is
  }
}