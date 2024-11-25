private String format(JSError error, boolean warning) {
  // extract source excerpt
  SourceExcerptProvider source = getSource();
  String sourceExcerpt = (source != null ? source.get(
      error.sourceName, error.lineNumber, excerptFormatter) : null);

  // formatting the message
  StringBuilder b = new StringBuilder();
  if (error.sourceName != null && !error.sourceName.isEmpty()) {
    b.append(error.sourceName);
    if (error.lineNumber > 0) {
      b.append(':');
      b.append(error.lineNumber);
    }
    b.append(": ");
  }

  b.append(getLevelName(warning ? CheckLevel.WARNING : CheckLevel.ERROR));
  b.append(" - ");
  
  b.append(error.description == null ? "" : error.description); // Ensure description is non-null
  b.append('\n');
  if (sourceExcerpt != null && !sourceExcerpt.isEmpty()) {
    b.append(sourceExcerpt);
    b.append('\n');
    
    int charno = error.getCharno();
    String formattedLine = formatLine(sourceExcerpt, error.lineNumber);

    // padding equal to the excerpt and arrow at the end
    // charno == sourceExpert.length() means something is missing
    // at the end of the line
    if (charno >= 0 && charno < formattedLine.length()) {
      for (int i = 0; i <= charno; i++) {
        b.append(formattedLine.charAt(i));
      }
      b.append("^\n");
    } else {
      // There's an error in the excerpt or char number
      b.append("Error: Invalid character number or source excerpt length.\n");
    }
  }
  return b.toString();
}