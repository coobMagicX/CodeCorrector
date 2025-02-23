private ExtractionInfo extractMultilineTextualBlock(JsDocToken token,
                                                    WhitespaceOption option) {
  if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
    return new ExtractionInfo("", token);
  }

  stream.update();
  int startLineno = stream.getLineno();
  int startCharno = stream.getCharno();

  StringBuilder builder = new StringBuilder();
  state = State.SEARCHING_ANNOTATION;

  boolean ignoreStar = false;

  do {
    switch (token) {
      case STAR:
        ignoreStar = !ignoreStar;
        break;

      case EOL:
        if (option != WhitespaceOption.SINGLE_LINE) {
          builder.append("\n");
        }
        ignoreStar = true;
        break;

      default:
        ignoreStar = false;
        if (token == JsDocToken.EOC || token == JsDocToken.EOF) {
          if (option != WhitespaceOption.PRESERVE) {
            while (builder.length() > 0 && builder.charAt(builder.length() - 1) == ' ') {
              builder.setLength(builder.length() - 1);
            }
          }

          int endLineno = stream.getLineno();
          int endCharno = stream.getCharno();

          String multilineText = builder.toString();
          jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
          return new ExtractionInfo(multilineText, token);
        }

        // Process annotation or other tokens not strictly part of the multiline text
        if (token == JsDocToken.ANNOTATION && option == WhitespaceOption.PRESERVE) {
          builder.append('\n').append(stream.getRemainingJSDocLine());
        } else {
          if (builder.length() > 0) {
            builder.append(' ');
          }
          builder.append(toString(token)).append(stream.getRemainingJSDocLine());
        }
    }

    token = next();
  } while (true);
}
