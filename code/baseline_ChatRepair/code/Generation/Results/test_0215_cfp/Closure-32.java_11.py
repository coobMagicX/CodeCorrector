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
    boolean isStartOfLine = true;

    while (token != JsDocToken.EOF && token != JsDocToken.EOC) {
        switch (token) {
            case STAR:
                if (!isStartOfLine) { // If not the start of a line, add '*'
                    builder.append('*');
                }
                isStartOfLine = false; // After a star, it can't be start of a newline
                break;

            case EOL:
                builder.append('\n');
                isStartOfLine = true; // A newline character flags next to be start
                break;

            case SPACE:
                if (!isStartOfLine) { // Only add spaces if it's not start of line
                    builder.append(' ');
                }
                break;

            default:
                if (!isStartOfLine && builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append(' ');
                }
                builder.append(stream.getRemainingJSDocLine().trim());
                isStartOfLine = false; // Once we append text, it's not start of a new line
                break;
        }
        token = next(); // Move to the next token regardless of the case
    }

    String multilineText = builder.toString();
    if (option != WhitespaceOption.PRESERVE) {
        multilineText = multilineText.trim(); // Additional trim if not preserving whitespace
    }

    int endLineno = stream.getLineno();
    int endCharno = stream.getCharno();

    if (multilineText.length() > 0) {
        jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
    }

    return new ExtractionInfo(multilineText, token);
}
