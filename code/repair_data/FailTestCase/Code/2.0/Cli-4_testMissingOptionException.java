    public void testMissingOptionException() throws ParseException {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create("f"));
        try {
            new PosixParser().parse(options, new String[0]);
            fail("Expected MissingOptionException to be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: f", e.getMessage());
        }
    }