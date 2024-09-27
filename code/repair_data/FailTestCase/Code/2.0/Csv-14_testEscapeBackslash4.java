    public void testEscapeBackslash4() throws IOException {
        StringWriter sw = new StringWriter();
        try (final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT.withQuote(QUOTE_CH))) {
            printer.print("\\\\");
        }
        assertEquals("\\\\", sw.toString());
    }