public String getLine(int lineNumber) {
  String js = "";
  try {
    // This is optimized for few warnings.
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  int pos = 0;
  int startLine = 1;

  // If we've saved a previous offset and it's for a line less than the
  // one we're searching for, then start at that point.
  if (lineNumber >= lastLine && lastOffset > 0) {
    pos = lastOffset;
    startLine = lastLine;
  }

  for (int n = startLine; n <= lineNumber; n++) { // Change condition to include the target line number
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
      return null; // If we reach the end of the string and haven't returned, it means we're on the last line
    }
    pos = nextpos + 1;
  }

  // Find the end of the line for the requested line number
  int endOfLinePos = js.indexOf('\n', pos);
  if (endOfLinePos == -1) {
    // If no newline character is found, we are at the last line and it does not have a newline at the end.
    endOfLinePos = js.length(); // Set position to the end of the string
  } else {
    endOfLinePos += 1; // Move past the newline character
  }

  // Remember this offset for the next search we do.
  lastOffset = endOfLinePos;
  lastLine = lineNumber;

  return js.substring(pos, endOfLinePos); // Return the content of the line
}