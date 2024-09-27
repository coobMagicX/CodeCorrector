public CSVPrinter(final Appendable out, final CSVFormat format) throws IOException {
    Assertions.notNull(out, "out");
    Assertions.notNull(format, "format");

    this.out = out;
    this.format = format;
    this.format.validate();
    this.headerPrinted = false; // Initialize headerPrinted flag
}

public void printRecord(final Object... values) throws IOException {
    if (!headerPrinted) {
        printHeader(); // Print the header if it hasn't been printed yet
        headerPrinted = true; // Set the headerPrinted flag to true
    }

    for (int i = 0; i < values.length; i++) {
        if (i > 0) {
            out.append(format.getDelimiter());
        }
        print(values[i]); // Print each value
    }
    println(); // Print a new line after each record
}

private void printHeader() throws IOException {
    if (format.getHeader() != null) {
        for (int i = 0; i < format.getHeader().length; i++) {
            if (i > 0) {
                out.append(format.getDelimiter());
            }
            print(format.getHeader()[i]); // Print each header value
        }
        println(); // Print a new line after the header
    }
}