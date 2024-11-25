public String getLine(int lineNumber) {
  String js = "";
  try {
    // NOTE(nicksantos): Right now, this is optimized for few warnings.
    // This is probably the right trade-off, but will be slow if there
    // are lots of warnings in one file.
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  int pos = 0;
  int startLine = 1;

  // If we've saved a previous offset and it's for a line less than the
  // one we're searching for, then start at that point.
  if (lineNumber >= lastLine) {
    pos = lastOffset;
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
  lastLine = lineNumber;

  int lineOffset = getLineOffset(lineNumber);

  int posAtLineNumber = js.indexOf('\n', lineOffset);
  if (posAtLineNumber == -1) {
    return null; // If requested line is at EOF, return null
  } else {
    return js.substring(lineOffset, posAtLineNumber + 1); // Return the whole line
  }
}