private String format(JSError error, boolean warning) {
    // extract source excerpt
    SourceExcerptProvider source = getSource();
    String sourceExcerpt = source == null ? null :
        source.get(
            error.sourceName, error.lineNumber, excerptFormatter);

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

        // Ensure padding corrects for whitespaces and places caret correctly
        if (0 <= charno && charno <= sourceExcerpt.length()) {
            for (int i = 0; i < charno; i++) {
                b.append(' ');
            }
            b.append('^');
            b.append('\n');
        }
    }
    return b.toString();
}