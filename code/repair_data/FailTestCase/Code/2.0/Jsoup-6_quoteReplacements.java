    @Test public void quoteReplacements() {
        String escaped = "&#92; &#36;";
        String unescaped = "\\ $";
        
        assertEquals(unescaped, Entities.unescape(escaped));
    }