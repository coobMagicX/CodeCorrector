    public void testSpecializedAsIntermediate() throws IOException {
        assertEquals(aposToQuotes("{'value':{'a':1,'b':2}}"),
                WRITER.writeValueAsString(new Bean1178Holder()));
    }