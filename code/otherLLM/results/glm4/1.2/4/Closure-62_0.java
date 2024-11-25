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

    // Padding equal to the excerpt length and arrow at the end
    // charno == sourceExpert.length() means something is missing at the end of the line
    if (charno >= 0 && charno < sourceExcerpt.length()) {
      for (int i = 0; i <= charno; i++) {
        b.append(sourceExcerpt.charAt(i));
      }
      // Adding a caret symbol after the error character
      b.append("^\n");
    }
  }
  return b.toString();
}