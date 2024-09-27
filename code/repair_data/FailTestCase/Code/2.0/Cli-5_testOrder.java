    public void testOrder() throws ParseException {
        Option optionA = new Option("a", "first");
        Options opts = new Options();
        opts.addOption(optionA);
        PosixParser posixParser = new PosixParser();
        CommandLine line = posixParser.parse(opts, null);
        assertFalse(line.hasOption(null));
    }