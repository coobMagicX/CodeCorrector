public String getLine(int lineNumber) {
    String js = "";
    try {
        js = getCode();
    } catch (IOException e) {
        return null;
    }

    // Utilize getLineOffset to find the exact start of the line
    int startOffset;
    try {
        startOffset = getLineOffset(lineNumber);
    } catch (IllegalArgumentException e) {
        return null; // Line number out of bounds
    }

    // Find the end of the line by locating the next newline character
    int endOffset = js.indexOf('\n', startOffset);
    if (endOffset == -1) {
        // If there's no newline, this might be the last line
        if (startOffset < js.length()) {
            return js.substring(startOffset);
        } else {
            return null; // Start offset is beyond the end of the string
        }
    } else {
        return js.substring(startOffset, endOffset);
    }
}