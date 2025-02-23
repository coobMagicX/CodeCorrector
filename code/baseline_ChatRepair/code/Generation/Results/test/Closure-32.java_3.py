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

  boolean ignoreStar = line.trim().isEmpty(); // Checks if the first line is empty and sets the flag

  do {
    switch (token) {
      case STAR:
        ignoreStar = (builder.length() == 0) || ignoreStar; // Only ignore star if it's the first in a newline
        if(!ignoreStar) {
          builder.append("*");
        }
        token = next();
        continue;

      case EOL:
        builder.append("\n");
        ignoreStar = true;
        token = next();
        continue;

      default:
        if (token == JsDocToken.EOC || token == JsDocToken.EOF || (token == JsDocToken.ANNOTATION && option != WhitespaceOption.PRESERVE)) {
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
        
        ignoreStar = false;
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
          builder.append(" ");
        }

        builder.append(toString(token)); // Append further tokens normally
        line = stream.getRemainingJSDocLine();
        if (option != WhitespaceOption.PRESERVE) {
          line = trimEnd(line);
        }
        builder.append(line);
        
        token = next();
    }
  } while (true);
}
