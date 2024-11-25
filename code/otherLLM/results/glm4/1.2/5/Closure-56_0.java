public String getLine(int lineNumber) {
  if (lineNumber <= 0) {
    return null; // Invalid line number.
  }

  int pos = 0;
  int startLine = 1;

  // If we've saved a previous offset and it's for a line less than the
  // one we're searching for, then start at that point.
  if (lineNumber >= lastLine) {
    pos = lastOffset;
    startLine = lastLine;
  }

  String js = getCode(); // Ensure this method is called as needed.

  for (int n = startLine; n < lineNumber; n++) {
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
      return null; // No more lines to read.
    }
    pos = nextpos + 1;
  }

  // Remember this offset for the next search we do.
  lastOffset = pos;
  lastLine = lineNumber;

  int endPos = js.indexOf('\n', pos);
  if (endPos == -1) {
    // If no new line is found, return everything from the current position to the end of the file.
    return js.substring(pos).trim(); // Trim trailing whitespace or newline at the end of the line.
  } else {
    // If a new line is found, return the content up to that point, trimmed.
    String lineContent = js.substring(pos, endPos).trim();
    if (lineContent.isEmpty()) {
      // If the line is empty, check for a blank line and skip it.
      while (js.indexOf('\n', pos) != -1 && js.indexOf('\r\n', pos) == -1) {
        pos = js.indexOf('\n', pos) + 1;
        endPos = js.indexOf('\n', pos);
        if (endPos == -1) {
          return null; // No more lines to read.
        }
      }
      pos = endPos + 1;
      lineContent = js.substring(pos, endPos).trim(); // Get the next non-empty line content.
    }
    return lineContent;
  }
}