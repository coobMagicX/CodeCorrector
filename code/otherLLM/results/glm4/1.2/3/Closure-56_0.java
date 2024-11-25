public String getLine(int lineNumber) {
  int pos = 0;
  int startLine = 1;

  // Use the existing line offsets if available.
  if (lineNumber >= lastLine && lastOffset != -1) {
    pos = getLineOffset(lastLine);
    startLine = lastLine;
  }

  for (int n = startLine; n < lineNumber; n++) {
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
      return null; // Return null if the line does not exist.
    }
    pos = nextpos + 1;
  }

  // If the specified line number is beyond the end of file content.
  if (lineNumber > js.length()) {
    return null;
  }

  // Remember this offset for the next search we do, including updating lastLine to reflect the current line number.
  lastOffset = pos;
  lastLine = lineNumber;

  // Get the end position of the line content.
  int endpos = js.indexOf('\n', pos);
  if (endpos == -1) {
    endpos = js.length(); // If there is no newline character, consider the end of file as the end of this line.
  }

  return js.substring(pos, endpos); // Return the content from current position to the next newline or end of file.
}