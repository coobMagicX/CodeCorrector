private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    // Set up the stream and initial positions
    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno();

    StringBuilder builder = new StringBuilder();
    Boolean firstLine = true;
    Boolean ignoreStar = false;

    // Continue reading and appending content based on tokens
    while (true) {
        switch (token) {
            case STAR:
                if (!ignoreStar && !firstLine) {  // Ignore first STAR if it is the first character on a new line
                    builder.append('*');
                }
                break;
            case EOL:
                if (option != WhitespaceOption.SINGLE_LINE) {
                    builder.append('\n');
                }
                ignoreStar = true;
                firstLine = true;
                break;
            case EOC:
            case EOF:
                // Trim if needed and finalize the extraction
                String resultText = option == WhitespaceOption.PRESERVE ? builder.toString() : builder.toString().trim();
                return new ExtractionInfo(resultText, token);
            default:
                if (!ignoreStar || !firstLine) {
                    builder.append(' ');
                }
                ignoreStar = false;
                firstLine = false;
                if (token == JsDocToken.ANNOTATION && option == WhitespaceOption.PRESERVE) {
                    builder.append('@');
                }
                builder.append(stream.getRemainingJSDocLine().trim()); // trim each line's trailing spaces
                break;
        }
        token = stream.next();  // Move to next token
    }
}
