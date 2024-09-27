    public void testOptionGroupLong() throws Exception
    {
        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.withLongOpt("foo").create());
        group.addOption(OptionBuilder.withLongOpt("bar").create());
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        CommandLine cl = parser.parse(options, new String[] { "--bar" });
        
        assertTrue(cl.hasOption("bar"));
        assertEquals("selected option", "bar", group.getSelected());
    }
