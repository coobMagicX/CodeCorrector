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

    // padding equal to the excerpt and arrow at the end
    // calculate correct length for padding
    int lineLength = formatLine(error.sourceLine, error.lineNumber).length();
    if (excerpt.equals(LINE)
        && 0 <= charno && charno < sourceExcerpt.length()) {
      b.append(formatLine(sourceExcerpt.substring(0, charno), error.lineNumber));
      b.append("^\n");
    } else {
      // extract relevant characters from source excerpt
      String padded = sourceExcerpt.substring(Math.max(charno - lineLength, 0),
          Math.min(charno + 1, sourceExcerpt.length()));
      b.append(padded);
      b.append("^\n");
    }
  }
  return b.toString();
}