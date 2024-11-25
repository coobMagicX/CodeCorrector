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
    pos = getLineOffset(lineNumber);
    startLine = lineNumber;
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

  if (js.indexOf('\n', pos) == -1 || pos >= js.length()) {
    // If the end of file is reached or there is no new line after pos, return null
    // for the case where pos reaches the end of the file.
    return null;
  } else {
    int nextNewLinePos = js.indexOf('\n', pos);
    if (nextNewLinePos == -1) {
      // If we reach the end of the file without finding a new line, return the rest
      // of the string from pos to the end of the file.
      return js.substring(pos);
    } else {
      // Otherwise, return the contents between pos and the next new line.
      return js.substring(pos, nextNewLinePos);
    }
  }
}