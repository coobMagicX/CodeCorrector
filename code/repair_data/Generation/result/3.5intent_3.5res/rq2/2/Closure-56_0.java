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

  if (js.indexOf('\n', pos) == -1) {
    // If next new line cannot be found, there are two cases
    // 1. pos already reaches the end of file, then null should be returned
    // 2. otherwise, return the contents between pos and the end of file.
    return null;
  } else {
    return js.substring(pos, js.indexOf('\n', pos));
  }
}

private int[] lineOffsets;

public void findLineOffsets() {
  // Implementation to find line offsets
}

public int getLineOffset(int lineno) {
  if (lineOffsets == null) {
    findLineOffsets();
  }
  if (lineno < 1 || lineno > lineOffsets.length) {
    throw new IllegalArgumentException(
        "Expected line number between 1 and " + lineOffsets.length +
        "\nActual: " + lineno);
  }
  return lineOffsets[lineno - 1];
}