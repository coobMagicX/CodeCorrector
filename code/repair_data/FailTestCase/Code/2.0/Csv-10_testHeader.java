    public void testHeader() throws IOException {
        final StringWriter sw = new StringWriter();
        final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT.withQuoteChar(null)
                .withHeader("C1", "C2", "C3"));
        printer.printRecord("a", "b", "c");
        printer.printRecord("x", "y", "z");
        assertEquals("C1,C2,C3\r\na,b,c\r\nx,y,z\r\n", sw.toString());
        printer.close();
    }