private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno();

    StringBuilder builder = new StringBuilder();
    boolean ignoreStar = false;

    while (true) {
        switch (token) {
            case STAR:
                if (!ignoreStar) {
                    if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                        builder.append(' ');
                    }
                    builder.append('*');
                }
                break;
            case EOL:
                builder.append("\n");
                ignoreStar = true;
                break;
            case EOC:
            case EOF:
                String multilineText = builder.toString();
                if (option != WhitespaceOption.PRESERVE) {
                    multilineText = multilineText.trim();
                }
                int endLineno = stream.getLineno();
                int endCharno = stream.getCharno();

                // Mark and return the capture
                jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
                return new ExtractionInfo(multilineText, token);
            default:
                ignoreStar = false;
                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append(' ');
                }
                builder.append(stream.getRemainingJSDocLine().trim());
                break;
        }

        token = stream.nextToken(); // Advance to the next token, correct method name as needed
    }
}
