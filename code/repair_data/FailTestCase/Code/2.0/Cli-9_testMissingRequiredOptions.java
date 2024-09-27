    public void testMissingRequiredOptions()
    {
        String[] args = new String[] { "-a" };

        _options.addOption( OptionBuilder.withLongOpt( "cfile" )
                                     .hasArg()
                                     .isRequired()
                                     .withDescription( "set the value of [c]" )
                                     .create( 'c' ) );

        try
        {
            CommandLine cl = parser.parse(_options,args);
            fail( "exception should have been thrown" );
        }
        catch (MissingOptionException e)
        {
            assertEquals( "Incorrect exception message", "Missing required options: b, c", e.getMessage() );
        }
        catch (ParseException e)
        {
            fail( "expected to catch MissingOptionException" );
        }
    }