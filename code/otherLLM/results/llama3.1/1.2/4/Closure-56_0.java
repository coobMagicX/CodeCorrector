public String getLine(int lineNumber) {
  if (!hasSourceInMemory()) {
    return null; // If source code is not in memory, cannot retrieve a line
  }

  String js = getCode();
  
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
    
    // Check if the line number is within valid bounds
    if (nextpos == -1 || lineNumber > js.length()) { 
      return null;
    }
    
    pos = nextpos + 1;
  }

  lastOffset = pos;
  lastLine = lineNumber;

  // If we've reached this point, then we have found the requested line number
  int nextNewLinePos = js.indexOf('\n', pos);
  
  if (nextNewLinePos == -1) {
    // If next new line cannot be found, there are two cases 
    // 1. pos already reaches the end of file, then null should be returned 
    // 2. otherwise, return the contents between pos and the end of file.
      return js.substring(pos); // Return everything from the current position to the end
  } else {
    return js.substring(pos, nextNewLinePos);
  }
}