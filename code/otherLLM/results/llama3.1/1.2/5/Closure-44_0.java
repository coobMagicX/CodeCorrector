void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    append(" ");
  }
  
  // Do not allow a forward slash to appear after a DIV.
  if (newcode.startsWith("/")) {
    append("\\/");
  }

  append(newcode);
}