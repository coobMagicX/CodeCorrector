    public void testSingleOptionMaximumNumberOfArgument() throws Exception {
        String[] args = new String[]{"-b", "1", "2"};
        final ArgumentBuilder abuilder = new ArgumentBuilder();
        final DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        final GroupBuilder gbuilder = new GroupBuilder();

        DefaultOption bOption = obuilder.withShortName("b")
                .withLongName("b")
                .withArgument(abuilder.withName("b")
                        .withMinimum(2)
                        .withMaximum(4)
                        .withDefault("100")
                        .withDefault("1000")
                        .withDefault("10000")
                        .create())
                .create();

        Group options = gbuilder
                .withName("options")
                .withOption(bOption)
                .create();

        Parser parser = new Parser();
        parser.setHelpTrigger("--help");
        parser.setGroup(options);
        CommandLine cl = parser.parse(args);
        CommandLine cmd = cl;
        assertNotNull(cmd);
        List b = cmd.getValues("-b");
        assertEquals("[1, 2, 10000]", b + "");
    }