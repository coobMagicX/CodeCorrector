void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  // Separate handling for leading forward slashes
  if (c == '/') {
    append(" ");
    append(c);
    int i = 1;
    while (i < newcode.length()) {
      char d = newcode.charAt(i++);
      if (d == '/' || isWordChar(d)) {
        append(" /");
      } else {
        break;
      }
    }
    return;
  }

  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    append(" ");
  }

  append(newcode);
}