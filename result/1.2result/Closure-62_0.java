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
    // charno == sourceExcerpt.length() means something is missing
    // at the end of the line
    if (excerpt.equals(LINE)
        && 0 <= charno && charno <= sourceExcerpt.length()) { // Ensure arrow can be placed at the very end
      for (int i = 0; i < charno; i++) {
        char c = sourceExcerpt.charAt(i);
        b.append(Character.isWhitespace(c) ? c : ' ');
      }
      b.append("^\n");
    }
  }
  return b.toString();
}