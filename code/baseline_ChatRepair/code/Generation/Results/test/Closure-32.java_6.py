private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno();

    StringBuilder builder = new StringBuilder();
    String line = stream.getRemainingJSDocLine();
    if (option != WhitespaceOption.PRESERVE) {
      line = line.trim();
    }
    builder.append(line);

    boolean ignoreStar = false;
    token = next();

    do {
        switch (token) {
            case STAR:
                if (!ignoreStar && builder.length() > 0) {
                    builder.append(' ');
                }
                ignoreStar = false;
                token = next();
                break;

            case EOL:
                builder.append("\n");
                ignoreStar = true;
                token = next();
                break;

            default:
                if (builder.length() > 0 && !ignoreStar) {
                    builder.append(' ');
                }

                if (token == JsDocToken.EOC || token == JsDocToken.EOF) {
                    String multilineText = builder.toString();
                    if (option != WhitespaceOption.PRESERVE) {
                        multilineText = multilineText.trim();
                    }
                    return new ExtractionInfo(multilineText, token);
                }

                String tokenText = toString(token);  // Add an appropriate method or logic to convert token to string
                builder.append(tokenText);

                if (option != WhitespaceOption.PRESERVE) {
                    line = stream.getRemainingJSDocLine().trim();
                } else {
                    line = stream.getRemainingJSDocLine();
                }

                builder.append(line);
                ignoreStar = false;
                token = next();
                break;
        }
    } while (token != JsDocToken.EOC && token != JsDocToken.EOF);

    return new ExtractionInfo(builder.toString().trim(), token);
}
