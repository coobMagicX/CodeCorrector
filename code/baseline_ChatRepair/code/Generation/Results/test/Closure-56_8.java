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
    pos = js.indexOf('\n', pos) + 1;
    if (pos == 0) { 
      return null;  
    }
  }

  
  lastOffset = pos;
  lastLine = lineNumber;

  int endPos = js.indexOf('\n', pos);
  if (endPos == -1) { 
    
    if (pos < js.length()) {
      return js.substring(pos);  
    } else {
      return null;  
    }
  } else {
    return js.substring(pos, endPos);  
  }
}
