void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  boolean needsSpace = false;

  if ((isWordChar(c) || c == '\\') && isWordChar(getLastChar())) {
    // need space to separate. This is not pretty printing.
    // For example: "return foo;"
    append(" ");
    needsSpace = true;
    
    // Do not allow a forward slash to appear after a DIV.
    // For example,
    // REGEXP DIV REGEXP
    // is valid and should print like
    // / // / /
    String trimmedNewcode = newcode.replaceAll("^/+", "").replaceAll("/+$", "");
    if (trimmedNewcode.startsWith("/")) {
      append("/");
      needsSpace = true;
    }
  }

  append(newcode);
}