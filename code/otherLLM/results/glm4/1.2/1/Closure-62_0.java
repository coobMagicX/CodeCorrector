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

    // Padding for the caret symbol and a newline at the end
    if (charno >= 0 && charno < sourceExcerpt.length()) {
      for (int i = 0; i <= charno; i++) { // Corrected to include the charno position
        b.append(' ');
      }
      b.append("^\n"); // Adding caret symbol
    } else if (charno < 0) {
      b.append("Error: Invalid character number\n");
    }
  }
  return b.toString();
}