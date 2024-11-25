public String getLine(int lineNumber) {
  int pos = 0;
  int startLine = 1;

  // If we've saved a previous offset and it's for a line less than the one we're searching for, then start at that point.
  if (lastLine != -1 && lineNumber >= lastLine) {
    pos = getLineOffset(lineNumber);
    startLine = lastLine;
  }

  for (int n = startLine; n < lineNumber; n++) {
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
      return null;
    }
    pos = nextpos + 1;
  }

  // Remember this offset for the next search we do.
  lastOffset = pos;

  // Find the end of the current line
  int endLinePos = js.indexOf('\n', pos);
  if (endLinePos == -1) {
    // If there is no newline character, the line extends to the end of the file
    endLinePos = js.length();
  }

  String lineContent = js.substring(pos, endLinePos);

  lastLine = lineNumber;
  return lineContent;
}