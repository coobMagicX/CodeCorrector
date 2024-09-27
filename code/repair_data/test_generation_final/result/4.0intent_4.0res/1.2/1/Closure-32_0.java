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

    boolean ignoreStar = false;

    do {
        switch (token) {
            case STAR:
                if (ignoreStar) {
                    // If ignoring star, skip adding it to the builder.
                } else {
                    if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                        builder.append(' '); // Ensure space before star if it's not the start of a new line
                    }
                    builder.append('*');
                }
                token = next();
                continue;

            case EOL:
                if (option != WhitespaceOption.SINGLE_LINE) {
                    builder.append("\n");
                }
                ignoreStar = true; // Set ignoreStar to true after an EOL to skip initial stars in the next line
                token = next();
                continue;

            default:
                ignoreStar = false;
                state = State.SEARCHING_ANNOTATION;

                if (token == JsDocToken.EOC || token == JsDocToken.EOF || token == JsDocToken.ANNOTATION) {
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

                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append(' '); // Ensure space before appending new content
                }
                
                builder.append(toString(token));
                line = stream.getRemainingJSDocLine();

                if (option != WhitespaceOption.PRESERVE) {
                    line = trimEnd(line);
                }

                builder.append(line);
                token = next();
        }
    } while (true);
}