public String getLine(int lineNumber) {
  int pos = 0;
  int startLine = 1;

  // Get the offset of the last line or reset to the beginning if lineNumber is less than 1.
  if (lineNumber >= 1) {
    pos = getLineOffset(lineNumber - 1);
    startLine = lineNumber;
  } else {
    return null; // Return null for invalid line numbers.
  }

  int currentLineEnd = pos;

  // Calculate the end of the current line by finding the next newline character.
  while (currentLineEnd < js.length() && js.charAt(currentLineEnd) != '\n') {
    currentLineEnd++;
  }
  if (js.charAt(currentLineEnd) == '\n') {
    currentLineEnd++; // Move past the newline character.
  }

  for (int n = startLine; n < lineNumber; n++) {
    pos = getLineOffset(n);
    if (pos == -1 || pos > currentLineEnd) { // If next line is beyond the end of the file, return null.
      return null;
    }
    int nextLineStart = js.indexOf('\n', currentLineEnd);
    if (nextLineStart == -1) {
      return null; // If no newline is found and we're at the end of the file, there's nothing left to read.
    } else {
      currentLineEnd = nextLineStart;
    }
  }

  // The start of the line we want is after the previous line's newline character.
  int startOfLine = js.indexOf('\n', pos);
  if (startOfLine == -1) {
    return null; // If there's no new line before, it means we're at the end of the file.
  }

  // Calculate the end of this line.
  int endOfLine = js.indexOf('\n', startOfLine + 1);
  if (endOfLine == -1) {
    endOfLine = js.length(); // If no newline is found, it's the end of the file.
  } else {
    endOfLine++; // Move past the newline character.
  }

  return js.substring(startOfLine + 1, endOfLine);
}