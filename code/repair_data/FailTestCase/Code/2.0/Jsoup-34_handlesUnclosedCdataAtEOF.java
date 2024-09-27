    @Test public void handlesUnclosedCdataAtEOF() {
        // https://github.com/jhy/jsoup/issues/349 would crash, as character reader would try to seek past EOF
        String h = "<![CDATA[]]";
        Document doc = Jsoup.parse(h);
        assertEquals(1, doc.body().childNodeSize());
    }