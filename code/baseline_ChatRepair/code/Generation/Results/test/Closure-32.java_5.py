private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
      return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno() + 1;

    StringBuilder builder = new StringBuilder();
    String line = stream.getRemainingJSDocLine();
    if (option != WhitespaceOption.PRESERVE) {
      line = line.trim();
    }
    builder.append(line);

    state = State.SEARCHING_ANNOTATION;
    token = next();

    boolean ignoreStar = line.trim().isEmpty();

    do {
      switch (token) {
        case STAR:
          ignoreStar = false;  // Assume presence of meaningful star until proven otherwise
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
            adjustFinalContent(builder, startLineno, startCharno, stream, jsdocBuilder, option);
            return new ExtractionInfo(builder.toString(), token);
          }

          String tokenText = token != JsDocToken.ANNOTATION ? toString(token) : "";
          builder.append(tokenText);
          
          line = stream.getRemainingJSDocLine();
          if (option != WhitespaceOption.PRESERVE) {
            line = trimEnd(line);
          }
          
          builder.append(line);
          ignoreStar = false;
          token = next();
      }
    } while (true);
}

private void adjustFinalContent(StringBuilder builder, int startLineno, int startCharno, JsDocTokenStream stream, JsDocBuilder jsdocBuilder, WhitespaceOption option) {
    String multilineText = builder.toString();
    if (option != WhitespaceOption.PRESERVE) {
      multilineText = multilineText.trim();
    }

    int endLineno = stream.getLineno();
    int endCharno = stream.getCharno();

    if (multilineText.length() > 0) {
      jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
    }
}
