import java.io.IOException;

public class CodeReader {
    private String codeCache;
    private int lastOffset = 0;
    private int lastLine = 1;

    public String getLine(int lineNumber) {
        if (lineNumber < 1) {
            return null; // Line numbers start at 1
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
            int nextpos = js.indexOf('\n', pos);
            if (nextpos == -1) {
                return null;
            }
            pos = nextpos + 1;
        }

        // Remember this offset for the next search we do.
        lastOffset = pos;
        lastLine = lineNumber;

        int endPos = js.indexOf('\n', pos);
        if (endPos == -1) {
            if (pos < js.length()) {
                return js.substring(pos); // Return from pos to the end of the file
            } else {
                return null; // If pos is at the end of the file, return null
            }
        } else {
            return js.substring(pos, endPos);
        }
    }

    private String getCode() throws IOException {
        // Dummy implementation to simulate getting code from a file
        if (this.codeCache == null) {
            this.codeCache = "This is the first line.\nThis is the second line.\nThis is the third line.";
        }
        return this.codeCache;
    }
}