void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char firstChar = newcode.charAt(0);
  
  if ((isWordChar(firstChar) || firstChar == '\\') && isWordChar(getLastChar())) {
    append(" ");
  } else if (firstChar == '/' && getLastChar() == '/') {
    
    append(" ");
  }

  append(newcode);
}
