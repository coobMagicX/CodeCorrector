private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
  if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
    return new ExtractionInfo("", token);
  }

  stream.update();
  int startLineno = stream.getLineno();
  int startCharno = stream.getCharno() + 1;

  String line = stream.getRemainingJSDocLine();
  if (option != WhitespaceOption.PRESERVE) {
    line = line.trim();
  }

  StringBuilder builder = new StringBuilder();
  builder.append(line);

  state = State.SEARCHING_ANNOTATION;
  token = next();

  boolean ignoreStar = true; // Changed initial value to true as we've already read the first line

  do {
    switch (token) {
      case STAR:
        if (!ignoreStar) {
          if (builder.length() > 0) {
            builder.append(' ');
          }
          builder.append('*');
        }
        token = next();
        break;

      case EOL:
        builder.append("\n");
        ignoreStar = true; // Next token after EOL should ignore a STAR if there's no content before it
        token = next();
        break;

      default:
        ignoreStar = false;
        if (token == JsDocToken.EOC || token == JsDocToken.EOF) {
          String multilineText = builder.toString().trim();

          int endLineno = stream.getLineno();
          int endCharno = stream.getCharno();

          if (multilineText.length() > 0) {
            jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
          }

          return new ExtractionInfo(multilineText, token);
        }

        if (builder.length() > 0) {
          builder.append(' ');
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
