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

  int pos = getLineOffset(lineNumber);
  
  int nextpos = js.indexOf('\n', pos);
  if (nextpos == -1) {
    return null;
  }
  String line = js.substring(pos, nextpos + 1);

  // Remember this offset for the next search we do.
  lastOffset = nextpos + 1;
  lastLine = lineNumber;

  return line;
}