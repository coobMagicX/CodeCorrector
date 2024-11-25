void add(String newcode) {
  maybeEndStatement();

  if (newcode.length() == 0) {
    return;
  }

  char c = newcode.charAt(0);

  // Check for spaces before a sequence of forward slashes.
  boolean needsSpace = false;
  for (int i = 0; i < newcode.length(); i++) {
    char currentChar = newcode.charAt(i);
    if (currentChar == ' ') {
      // If we find a space, set the flag to need a space but skip consecutive spaces
      needsSpace = true;
    } else if ((isWordChar(currentChar) || c == '\\') &&
                (i > 0 && isWordChar(newcode.charAt(i - 1)))) {
      // If a word character or backslash is followed by another word character,
      // we don't need to add a space since the characters are already separated.
      needsSpace = false;
    } else if (currentChar == '/' && needsSpace) {
      // Append a space before slashes to separate them properly
      append(" ");
      needsSpace = false; // Reset the flag after appending a space
    }
  }

  append(newcode);
}