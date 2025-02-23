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

    state = State.SEARCHING_ANNOTATION;
    token = next();

    boolean ignoreStar = line.isEmpty() || Character.isWhitespace(line.charAt(0));
    
    do {
        switch (token) {
            case STAR:
                // After moving to the new line, if first non-whitespace character is '*', it's part of the block.
                if (!ignoreStar) {
                    if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                        builder.append(' ');
                    }
                    builder.append('*');
                }
                ignoreStar = false;
                token = next();
                continue;

            case EOL:
                builder.append("\n");
                // We expect potential stars on new lines
                ignoreStar = true;
                token = next();
                continue;

            default:
                ignoreStar = false;
                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append(' ');
                }
                builder.append(stream.getRemainingJSDocLine().trim());
                token = next();
        }
    } while (token != JsDocToken.EOF && token != JsDocToken.EOC);

    // Cleanup and format the final text
    String multilineText = builder.toString();
    if (option != WhitespaceOption.PRESERVE) {
        multilineText = multilineText.trim();
    }

    int endLineno = stream.getLineno();
    int endCharno = stream.getCharno();

    if (multilineText.length() > 0) {
        jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
    }

    return new ExtractionInfo(multilineText, token);
}
