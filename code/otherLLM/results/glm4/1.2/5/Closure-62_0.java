private String format(JSError error, boolean warning) {
  // extract source excerpt
  SourceExcerptProvider source = getSource();
  String sourceExcerpt = source == null ? null :
      excerpt.get(
          source, error.sourceName, error.lineNumber, excerptFormatter);

  // formatting the message
  StringBuilder b = new StringBuilder();
  if (error.sourceName != null) {
    b.append(error.sourceName);
    if (error.lineNumber > 0) {
      b.append(':');
      b.append(error.lineNumber);
    }
    b.append(": ");
  }

  b.append(getLevelName(warning ? CheckLevel.WARNING : CheckLevel.ERROR));
  b.append(" - ");
  b.append(error.description);
  b.append('\n');

  if (sourceExcerpt != null) {
    b.append(sourceExcerpt);
    b.append('\n');
    int charno = error.getCharno();
    int endCharNo = sourceExcerpt.length();

    // Determine padding length
    int paddingLength = Math.max(0, endCharNo - charno);

    // Append the appropriate amount of whitespace padding and caret symbol at the end
    for (int i = 0; i < paddingLength; i++) {
      b.append(' ');
    }
    if (charno >= 0 && charno < endCharNo) { // Ensure charno is within source excerpt bounds
      b.append("^\n");
    } else {
      b.append("\n"); // Append a newline if no caret symbol should be displayed
    }
  }

  return b.toString();
}