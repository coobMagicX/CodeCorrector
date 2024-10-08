public String getLine(int lineNumber) {
  String js = "";
  try {
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  int pos = 0;
  int startLine = 1;

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

  lastOffset = pos;
  lastLine = lineNumber;

  if (pos >= js.length()) {
    return null;
  } else {
    int nextLine = js.indexOf('\n', pos);
    if (nextLine == -1) {
      return js.substring(pos);
    } else {
      return js.substring(pos, nextLine);
    }
  }
}