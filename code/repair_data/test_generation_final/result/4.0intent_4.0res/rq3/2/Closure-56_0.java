import java.io.IOException;

public class CodeReader {
    private String codeCache;
    private int lastLine = 1;
    private int lastOffset = 0;

    public String getCode() throws IOException {
        // This method is supposed to return the contents of a code file as a String
        // The actual implementation to read the file content is not shown here for simplicity
        return "Example code content\nNext line of code\nAnother line of code\nLast line";
    }

    public String getLine(int lineNumber) {
        if (lineNumber < 1) {
            return null; // Check for invalid line numbers
        }

        String js = "";
        try {
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
            pos = js.indexOf('\n', pos) + 1;
            if (pos == 0) {
                return null; // End of string reached without finding lineNumber
            }
        }

        // Remember this offset for the next search we do.
        lastOffset = pos;
        lastLine = lineNumber;

        int nextNewLinePos = js.indexOf('\n', pos);
        if (nextNewLinePos == -1) {
            if (pos >= js.length()) {
                return null; // pos is at or beyond end of string
            } else {
                return js.substring(pos); // return from pos to end of string
            }
        } else {
            return js.substring(pos, nextNewLinePos);
        }
    }
}