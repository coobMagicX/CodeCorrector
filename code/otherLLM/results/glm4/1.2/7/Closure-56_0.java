public String getLine(int lineNumber) {
  try {
    // Retrieve the code with warnings if present.
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  int pos = 0;
  int startLine = 1;

  // If we've saved a previous offset and it's for a line less than or equal to the
  // one we're searching for, then start at that point.
  if (lineNumber >= lastLine) {
    pos = lastOffset;
    startLine = lastLine;
  }

  // Find the index of the newline character for each line up to the desired line number.
  int targetPos = js.indexOf('\n', pos);
  while (targetPos != -1 && startLine < lineNumber) {
    pos = targetPos + 1;
    targetPos = js.indexOf('\n', pos);
    startLine++;
  }

  // If we've reached or passed the desired line number, get the substring up to the next newline character.
  if (startLine == lineNumber) {
    if (targetPos != -1) {
      return js.substring(pos, targetPos);
    } else {
      // If there's no new line after this one and it's not the last line in the file,
      // we should still be able to return the content of the current line.
      int endOfFile = js.length();
      if (js.charAt(endOfFile - 1) != '\n') {
        return null;
      }
      return js.substring(pos);
    }
  }

  // If we've reached or passed the desired line number and there's no newline character,
  // it means we're at the end of the file without a new line, so we return null.
  return null;
}