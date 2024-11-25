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
    
    // Check for consecutive forward slashes and add an additional slash
    int index = newcode.indexOf("//");
    if (index != -1) {
      String remainingNewcode = newcode.substring(index + 2);
      append("//");
      append(remainingNewcode);
    } else {
      append(newcode);
    }
    
  } else {
    append(newcode);
  }
}