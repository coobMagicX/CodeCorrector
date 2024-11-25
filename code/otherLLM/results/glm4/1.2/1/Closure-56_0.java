public String getLine(int lineNumber) {
    int pos = 0;
    int startLine = 1;

    // If we've saved a previous offset and it's for a line less than the one we're searching for, then start at that point.
    if (lineNumber >= lastLine) {
        pos = getLineOffset(lastLine);
        startLine = lastLine;
    }

    // Initialize line number to find
    int currentLine = startLine;

    while (currentLine < lineNumber) {
        String nextLineEnd = js.substring(pos).startsWith("\n") ? "\n" : "";

        // Find the index of the end of the current line or the newline character after it
        int nextpos = js.indexOf('\n', pos);
        if (nextpos == -1) {
            return null; // End of file reached without finding enough lines
        }

        // If the next character is not a newline, we are at the end of the file
        if (!js.substring(nextpos).startsWith("\n")) {
            return js.substring(pos);
        }

        // Move to the start of the next line
        pos = nextpos + 1;
        currentLine++;
    }

    // If the current line is the one we're looking for, extract its content
    if (currentLine == lineNumber) {
        int endPos = js.indexOf('\n', pos);
        // If it's the last line in the file, there won't be another newline character
        return endPos == -1 ? js.substring(pos) : js.substring(pos, endPos);
    }

    // This point should never be reached because we already check if the line number is within range
    return null;
}