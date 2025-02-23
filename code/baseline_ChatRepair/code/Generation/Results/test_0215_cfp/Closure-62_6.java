private String format(JSError error, boolean warning) {
  
  SourceExcerptProvider source = getSource();
  String sourceExcerpt = source == null ? null :
      source.getSourceLine(error.sourceName, error.lineNumber);

  
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

    
    if (0 <= charno && charno <= sourceExcerpt.length()) {
      for (int i = 0; i < charno; i++) {
        b.append(sourceExcerpt.charAt(i) == '\t' ? '\t' : ' ');
      }
      b.append("^\n");
    }
  }
  return b.toString();
}
