public CSVPrinter(final Appendable out, final CSVFormat format) throws IOException {
    Assertions.notNull(out, "out");
    Assertions.notNull(format, "format");

    this.out = out;
    this.format = format;
    this.format.validate();
    
    // Print the header
    printHeader();
}

private void printHeader() throws IOException {
    final String[] header = format.getHeader();
    if (header != null) {
        for (int i = 0; i < header.length; i++) {
            if (i > 0) {
                out.append(format.getDelimiter());
            }
            out.append(header[i]);
        }
        println();
    }
}