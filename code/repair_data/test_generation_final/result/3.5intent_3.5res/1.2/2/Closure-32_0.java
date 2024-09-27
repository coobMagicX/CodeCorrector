private ExtractionInfo extractMultilineTextualBlock(JsDocToken token,
                                                    WhitespaceOption option) {

  if (token == JsDocToken.EOC || token == JsDocToken.EOL ||
      token == JsDocToken.EOF) {
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
          // Mark the position after the star as the new start of the line.
        } else {
          // The star is part of the comment.
          if (builder.length() > 0) {
            builder.append(' ');
          }

          builder.append('*');
        }

        token = next();
        continue;

      case EOL:
        if (option != WhitespaceOption.SINGLE_LINE) {
          builder.append("\n");
        }

        ignoreStar = true;
        token = next();
        continue;

      default:
        ignoreStar = false;
        state = State.SEARCHING_ANNOTATION;

        if (token == JsDocToken.EOC ||
            token == JsDocToken.EOF ||
            (token == JsDocToken.ANNOTATION &&
             option != WhitespaceOption.PRESERVE)) {
          String multilineText = builder.toString();

          if (option != WhitespaceOption.PRESERVE) {
            multilineText = multilineText.trim();
          }

          int endLineno = stream.getLineno();
          int endCharno = stream.getCharno();

          if (multilineText.length() > 0) {
            jsdocBuilder.markText(multilineText, startLineno, startCharno,
                endLineno, endCharno);
          }

          return new ExtractionInfo(multilineText, token);
        }

        if (builder.length() > 0) {
          builder.append(' ');
        }
        builder.append(toString(token));

        line = stream.getRemainingJSDocLine();

        if (option != WhitespaceOption.PRESERVE) {
          line = line.trim();
        }

        builder.append(" ");
        builder.append(line);
        token = next();
    }
  } while (true);
}