public String getLine(int lineNumber) {
    String js = "";
    try {
        js = getCode();
    } catch (IOException e) {
        return null;
    }

    int pos = 0;
    int currentLine = 1;

    
    if (lineNumber >= lastLine) {
        pos = lastOffset;
        currentLine = lastLine;
    }

    
    while (currentLine < lineNumber) {
        int nextPos = js.indexOf('\n', pos);
        if (nextPos == -1) {
            return null; 
        }
        pos = nextPos + 1;
        currentLine++;
    }

    
    lastOffset = pos;
    lastLine = lineNumber;

    
    int endPos = js.indexOf('\n', pos);
    if (endPos == -1) {
        if (pos < js.length()) {
            return js.substring(pos); 
        }
        return null; 
    } else {
        return js.substring(pos, endPos); 
    }
}
