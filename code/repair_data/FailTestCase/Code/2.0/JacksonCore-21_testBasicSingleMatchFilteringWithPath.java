    public void testBasicSingleMatchFilteringWithPath() throws Exception
    {
        JsonParser p0 = JSON_F.createParser(SIMPLE);
        JsonParser p = new FilteringParserDelegate(p0,
               new NameMatchFilter("value"),
                   true, // includePath
                   false // multipleMatches
                );

// {'a':123,'array':[1,2],'ob':{'value0':2,'value':3,'value2':4},'b':true}
        String result = readAndWrite(JSON_F, p);
        assertEquals(aposToQuotes("{'ob':{'value':3}}"), result);
    }