public String getLine(int lineNumber) {
  String js = "";
  try {
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  int pos = 0;
  int startLine = 1;

  // Using previously saved state if it matches the criteria
  if (lineNumber >= lastLine) {
    pos = lastOffset;
    startLine = lastLine;
  }

  // Locate the start position of the desired line
  for (int n = startLine; n < lineNumber; n++) {
    int nextpos = js.indexOf('\n', pos);
    if (nextpos == -1) {
      return null;  // Line number does not exist.
    }
    pos = nextpos + 1;
  }

  // Save this state to potentially speed up future searches
  lastOffset = pos;
  lastLine = lineNumber;

  int endPos = js.indexOf('\n', pos);
  if (endPos == -1) {
    if (pos >= js.length()) {
      return null; // pos is outside the content range of js
    }
    // Return substring from pos to the end of the string as there's no newline.
    return js.substring(pos);
  } else {
    return js.substring(pos, endPos);
  }
}
