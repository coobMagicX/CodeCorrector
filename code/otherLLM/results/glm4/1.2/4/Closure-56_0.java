public String getLine(int lineNumber) {
  if (lineNumber < 1) {
    return null;
  }

  int pos = getLineOffset(lineNumber);

  if (pos == -1) { // This means the line number is out of range or invalid.
    return null;
  }

  String js = getCode(); // We assume this method returns the full code as a string.

  if (js == null) {
    return null; // In case the file reading fails, we should return null as well.
  }

  int endLine = lineNumber + 1;
  int endPos = getLineOffset(endLine);

  if (endPos == -1 || pos >= endPos) { // If the line does not exist or is at the end of file
    return js.substring(pos); // Return from the last known offset to the end of the string
  } else {
    // Otherwise, find the newline character that marks the end of this line.
    int endOfLine = js.indexOf('\n', pos);
    if (endOfLine == -1) { // If no new line is found after the current position, return null
      return null;
    }
    return js.substring(pos, endOfLine);
  }
}