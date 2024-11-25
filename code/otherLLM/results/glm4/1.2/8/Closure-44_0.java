void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  StringBuilder sb = new StringBuilder();
  for (int i = 0; i < newcode.length(); i++) {
    char c = newcode.charAt(i);
    if (c == '/' && i > 0 && newcode.charAt(i - 1) != '\\') { // Check for a forward slash not preceded by an escape character
      sb.append(c).append(' '); // Append the slash and a space after it
    } else {
      sb.append(c); // Append other characters directly
    }
  }

  String fixedNewcode = sb.toString();
  append(fixedNewcode);
}