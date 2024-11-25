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
    // charno == sourceExpert.length() means something is missing
    // at the end of the line
    if (excerpt.equals(LINE)
        && 0 <= charno && charno < sourceExcerpt.length()) {
      for (int i = 0; i < charno; i++) {
        b.append(sourceExcerpt.charAt(i));
      }
      b.append("^\n");
    } else {
      // pad the excerpt with spaces to match error description length
      int excerptLength = sourceExcerpt != null ? sourceExcerpt.length() : 0;
      for (int i = 0; i < Math.max(0, error.description.length() - excerptLength); i++) {
        b.append(' ');
      }
      b.append(sourceExcerpt);
      b.append('\n');
    }
  }
  return b.toString();
}