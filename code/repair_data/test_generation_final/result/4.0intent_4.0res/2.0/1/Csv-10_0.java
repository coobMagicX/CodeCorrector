public CSVPrinter(final Appendable out, final CSVFormat format) throws IOException {
    Assertions.notNull(out, "out");
    Assertions.notNull(format, "format");

    this.out = out;
    this.format = format;
    this.format.validate();
    // Check and print headers only if they are not supposed to be skipped
    if (this.format.getHeader() != null && !this.format.getSkipHeaderRecord()) {
        for (int i = 0; i < this.format.getHeader().length; i++) {
            if (i > 0) {
                this.out.append(this.format.getDelimiter());
            }
            this.print(this.format.getHeader()[i]);
        }
        this.println();
    }
}