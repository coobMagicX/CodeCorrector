    public void testSkippingForSingleWithPath() throws Exception
    {
        JsonParser p0 = JSON_F.createParser(SIMPLE);
        JsonParser p = new FilteringParserDelegate(p0,
               new NameMatchFilter("value"),
                   true, // includePath
                   false // multipleMatches
                );

//        assertEquals(aposToQuotes("{'ob':{'value':3}}"), result);

        assertToken(JsonToken.START_OBJECT, p.nextToken());
        p.skipChildren();
        assertEquals(JsonToken.END_OBJECT, p.getCurrentToken());
        assertNull(p.nextToken());
    }