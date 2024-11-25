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

  // Track the start of the line to count whitespace that
  // the tokenizer skipped. Because this case is rare, it's easier
  // to do this here than in the tokenizer.

  do {
    switch (token) {
      case STAR:
        if (ignoreStar) {
          // Mark the position after the star as the new start of the line.
          ignoreStar = false;
        } else {
          // The star is part of the comment.
          if (builder.length() > 0 && !line.endsWith(" ")) {
            builder.append(' ');
          }
          builder.append('*');
        }

        token = next();
        continue;

      case EOL:
        if (option != WhitespaceOption.SINGLE_LINE) {
          builder.append("\n");
        } else {
          // For single-line option, do not add newline character.
          ignoreStar = false;
        }
        token = next();
        continue;

      default:
        ignoreStar = false;
        state = State.SEARCHING_ANNOTATION;

            // All tokens must be separated by a space.

        if (token == JsDocToken.EOC ||
            token == JsDocToken.EOF ||
            (option != WhitespaceOption.PRESERVE && (token == JsDocToken.ANNOTATION || token == JsDocToken.COMMENT))) {
          String multilineText = builder.toString();

          // Trim the text only if preserving whitespace is not an option
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

        line = stream.getRemainingJSDocLine();
        if (option != WhitespaceOption.PRESERVE) {
          line = trimEnd(line);
        }
        builder.append(line);
        token = next();
    }
  } while (true);
}