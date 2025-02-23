private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
  if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
    return new ExtractionInfo("", token);
  }

  stream.update();
  int startLineno = stream.getLineno();
  int startCharno = stream.getCharno() + 1;

  StringBuilder builder = new StringBuilder();
  boolean nextLineStart = true; // This flag will track whether we are at the beginning of a new line

  do {
    if (token == JsDocToken.STAR && nextLineStart) {
      // If the token is STAR and at the beginning of a new line, ignore it.
      token = next();
      continue;
    }

    // If we're here, we're not at the immediate start of a new line
    nextLineStart = false;

    switch (token) {
      case EOL:
        builder.append("\n");
        nextLineStart = true; // After an EOL, the next content will be at the start of a new line
        token = next();
        continue;

      case EOF:
      case EOC:
        // Trim and finalize the extracted text upon encountering the end of comments
        String multilineText = option == WhitespaceOption.PRESERVE ? builder.toString() : builder.toString().trim();
        int endLineno = stream.getLineno();
        int endCharno = stream.getCharno();
        
        // Optionally, mark the text in the builder, adjust this portion should you utilize a method like markText
        if (multilineText.length() > 0) {
          jsdocBuilder.markText(multilineText, startLineno, startCharno, endLineno, endCharno);
        }

        return new ExtractionInfo(multilineText, token);

      default:
        // Add current token's text to the builder
        String textToAdd = toString(token);
        if (option == WhitespaceOption.PRESERVE) {
          builder.append(textToAdd);
        } else {
          // Only trim leading spaces when not preserving whitespace
          // This can be adjusted based on exact whitespace handling needs
          if (builder.length() == 0) {
            textToAdd = textToAdd.trim();
          }
          builder.append(textToAdd);
        }
        token = next();
    }
  } while (token != JsDocToken.EOF); // continue until end of file token is received

  return new ExtractionInfo(builder.toString(), JsDocToken.EOF); // Fallback return, never expected to reach here
}
