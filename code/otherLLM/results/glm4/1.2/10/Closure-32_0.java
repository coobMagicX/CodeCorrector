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
          builder.append('*');
        }

        token = next();
        continue;

      case EOL:
        if (option != WhitespaceOption.SINGLE_LINE) {
          builder.append("\n");
        }
        ignoreStar = true; // After a newline, any following asterisk should be ignored
        token = next();
        continue;

      default:
        ignoreStar = false;
        state = State.SEARCHING_ANNOTATION;

        if (token == JsDocToken.EOC ||
            token == JsDocToken.EOF ||
            (token == JsDocToken.ANNOTATION && option != WhitespaceOption.PRESERVE)) {
          String multilineText = builder.toString();

          if (option != WhitespaceOption.PRESERVE) {
            multilineText = multilineText.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
          }

          int endLineno = stream.getLineno();
          int endCharno = stream.getCharno();

          if (multilineText.length() > 0) {
            jsdocBuilder.markText(multilineText, startLineno, startCharno,
                endLineno, endCharno);
          }

          return new ExtractionInfo(multilineText, token);
        }

        builder.append(' ');
        builder.append(toString(token));

        if (option != WhitespaceOption.PRESERVE) {
          line = stream.getRemainingJSDocLine();
          line = trimEnd(line); // Trim only the end of the current line
        } else {
          line = stream.getRemainingJSDocLine(); // Preserve whitespace, no trimming needed
        }

        builder.append(line);
        token = next();
    }
  } while (true);
}