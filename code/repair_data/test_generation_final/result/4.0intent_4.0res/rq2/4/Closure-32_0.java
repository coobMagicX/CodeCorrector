private ExtractionInfo extractMultilineTextualBlock(JsDocToken token,
                                                    WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno();

    // Read the content from the first line.
    String line = stream.getRemainingJSDocLine();
    if (option != WhitespaceOption.PRESERVE) {
        line = line.trim();
    }

    StringBuilder builder = new StringBuilder();
    builder.append(line);

    state = State.SEARCHING_ANNOTATION;
    token = next();

    boolean ignoreStar = true;

    do {
        switch (token) {
            case STAR:
                if (!ignoreStar) {
                    // Include the star in the comment content.
                    if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                        builder.append(' ');
                    }
                    builder.append('*');
                }
                token = next();
                continue;

            case EOL:
                builder.append("\n");
                ignoreStar = true;
                token = next();
                continue;

            case EOC:
            case EOF:
            case ANNOTATION:
                if (option != WhitespaceOption.PRESERVE) {
                    String multilineText = builder.toString().trim();
                    int endLineno = stream.getLineno();
                    int endCharno = stream.getCharno();
                    jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
                    return new ExtractionInfo(multilineText, token);
                } else {
                    String multilineText = builder.toString();
                    int endLineno = stream.getLineno();
                    int endCharno = stream.getCharno();
                    jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
                    return new ExtractionInfo(multilineText, token);
                }

            default:
                ignoreStar = false;
                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append(' ');
                }
                builder.append(toString(token));
                line = stream.getRemainingJSDocLine();
                if (option != WhitespaceOption.PRESERVE) {
                    line = line.trim();
                }
                builder.append(line);
                token = next();
        }
    } while (true);
}