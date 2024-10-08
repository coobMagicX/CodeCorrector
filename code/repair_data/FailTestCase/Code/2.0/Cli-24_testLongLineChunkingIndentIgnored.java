    public void testLongLineChunkingIndentIgnored() throws ParseException, IOException {
        Options options = new Options();
        options.addOption("x", "extralongarg", false, "This description is Long." );
        HelpFormatter formatter = new HelpFormatter();
        StringWriter sw = new StringWriter();
        formatter.printHelp(new PrintWriter(sw), 22, this.getClass().getName(), "Header", options, 0, 5, "Footer");
        String expected = "usage:\n" +
                          "       org.apache.comm\n" +
                          "       ons.cli.bug.Bug\n" +
                          "       CLI162Test\n" +
                          "Header\n" +
                          "-x,--extralongarg\n" +
                          "                     T\n" +
                          "                     h\n" +
                          "                     i\n" +
                          "                     s\n" +
                          "                     d\n" +
                          "                     e\n" +
                          "                     s\n" +
                          "                     c\n" +
                          "                     r\n" +
                          "                     i\n" +
                          "                     p\n" +
                          "                     t\n" +
                          "                     i\n" +
                          "                     o\n" +
                          "                     n\n" +
                          "                     i\n" +
                          "                     s\n" +
                          "                     L\n" +
                          "                     o\n" +
                          "                     n\n" +
                          "                     g\n" +
                          "                     .\n" +
                          "Footer\n";
        assertEquals( "Long arguments did not split as expected", expected, sw.toString() );
    }