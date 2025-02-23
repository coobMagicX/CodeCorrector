private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno() + 1;

    // Read the content from the first line.
    String line = stream.getRemainingJSDocLine();
    if (option != WhitespaceOption.PRESERVE) {
        line = line.trim();
    }

    StringBuilder builder = new StringBuilder();
    builder.append(line);

    token = next();
    boolean startOfLine = true; // track the beginning of each line

    do {
        switch (token) {
            case EOL:
                builder.append("\n");
                startOfLine = true; // next token should consider if it's at a new line start.
                token = next();
                break;

            case STAR:
                if (!startOfLine) {
                    // If not at start of line, it's part of the content.
                    builder.append('*');
                }
                startOfLine = false; // Regardless of evaluation, move out of startOfLine.
                token = next();
                break;

            default:
                // Handle other characters.
                if (startOfLine && Character.isWhitespace((char)token)) {
                    // if starting whitespace on non-newline start, skip it
                    token = next();
                    continue;
                }
                if (builder.length() > 0) {
                    builder.append(' '); // adding space before starting new content.
                }
                builder.append(stream.getRemainingJSDocLine().trim());
                startOfLine = false; // additional text means no longer at the start of the line
                token = next();
                break;
        }
    } while (token != JsDocToken.EOF && token != JsDocToken.EOC);

    // Finally trim and mark text if needed
    String multilineText = builder.toString().trim();

    int endLineno = stream.getLineno();
    int endCharno = stream.getCharno();

    if (multilineText.length() > 0) {
        jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
    }

    return new ExtractionInfo(multilineText, token);
}
