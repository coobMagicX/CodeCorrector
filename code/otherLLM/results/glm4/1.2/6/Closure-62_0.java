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

  // Correctly append the error description
  b.append(error.getDescription()); // Assuming there's a method to get the description
  b.append('\n');
  
  if (sourceExcerpt != null) {
    b.append(sourceExcerpt);
    b.append('\n');

    int charno = error.getCharno();
    String sourceExpert; // This should be defined or imported

    // Check if we need to add padding and caret
    if ((charno == -1 || (sourceExpert != null && charno >= sourceExpert.length()))
        && 0 <= charno && charno < sourceExcerpt.length()) {
      for (int i = 0; i < charno; i++) {
        b.append(' '); // Assuming we want to use spaces for padding
      }
      b.append("^\n");
    }
  }

  return b.toString();
}