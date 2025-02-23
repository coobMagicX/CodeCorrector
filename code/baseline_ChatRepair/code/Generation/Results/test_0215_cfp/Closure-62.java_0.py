private String format(JSError error, boolean warning) {
  // extract source excerpt
  SourceExcerptProvider source = getSource();
  String sourceExcerpt = source == null ? null :
      source.get(
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
    // handle charno at the end of the line
    if (excerpt.equals(LINE)) {
      int paddingLength = Math.min(charno, sourceExcerpt.length());
      for (int i = 0; i < paddingLength; i++) {
        char c = sourceExcerpt.charAt(i);
        if (Character.isWhitespace(c)) {
          b.append(c);
        } else {
          b.append(' ');
        }
      }
      if (charno <= sourceExcerpt.length()) {
        b.append("^\n");
      }
    }
  }
  return b.toString();
}
