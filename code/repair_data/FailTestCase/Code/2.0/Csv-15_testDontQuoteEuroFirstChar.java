    public void testDontQuoteEuroFirstChar() throws IOException {
        final StringWriter sw = new StringWriter();
        try (final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.RFC4180)) {
            printer.printRecord(EURO_CH, "Deux");
            assertEquals(EURO_CH + ",Deux" + recordSeparator, sw.toString());
        }
    }