    public void testAsEmbedded() throws Exception
    {
        JsonGenerator g;

        StringWriter sw = new StringWriter();
        g = JSON_F.createGenerator(sw);
        g.writeEmbeddedObject(null);
        g.close();
        assertEquals("null", sw.toString());

        ByteArrayOutputStream bytes =  new ByteArrayOutputStream(100);
        g = JSON_F.createGenerator(bytes);
        g.writeEmbeddedObject(null);
        g.close();
        assertEquals("null", bytes.toString("UTF-8"));

        // also, for fun, try illegal unknown thingy

        try {
            g = JSON_F.createGenerator(bytes);
            // try writing a Class object
            g.writeEmbeddedObject(getClass());
            fail("Expected an exception");
            g.close(); // never gets here
        } catch (JsonGenerationException e) {
            verifyException(e, "No native support for");
        }
    }