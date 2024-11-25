void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);
  // Check for trailing whitespace and remove it
  newcode = newcode.trim();
  
  if ((isWordChar(c) || c == '\\') &&
      isWordChar(getLastChar())) {
    append(" ");
    
    // Do not allow a forward slash to appear after a DIV.
    if (c == '/') {
      maybeCutLine(); // Cut the line before adding the newcode
    }
  }

  append(newcode);
}