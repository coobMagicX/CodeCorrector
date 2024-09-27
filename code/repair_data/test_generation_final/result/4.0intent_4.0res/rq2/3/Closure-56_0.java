public String getLine(int lineNumber) {
    String js = "";
    try {
        // NOTE: Optimized for fewer warnings, may be slow with many warnings.
        js = getCode();
    } catch (IOException e) {
        return null;
    }

    int pos = 0;
    int startLine = 1;

    // Use previous offset if it's for a line less than the one being searched.
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

    // Save this offset for the next search.
    lastOffset = pos;
    lastLine = lineNumber;

    if (js.indexOf('\n', pos) == -1) {
        if (pos >= js.length()) {
            return null;
        } else {
            return js.substring(pos);
        }
    } else {
        return js.substring(pos, js.indexOf('\n', pos));
    }
}