    @Test public void handlesInvalidDoctypes() {
        // would previously throw invalid name exception on empty doctype
        Document doc = Jsoup.parse("<!DOCTYPE>");
        assertEquals(
                "<!DOCTYPE> <html> <head></head> <body></body> </html>",
                StringUtil.normaliseWhitespace(doc.outerHtml()));

        doc = Jsoup.parse("<!DOCTYPE><html><p>Foo</p></html>");
        assertEquals(
                "<!DOCTYPE> <html> <head></head> <body> <p>Foo</p> </body> </html>",
                StringUtil.normaliseWhitespace(doc.outerHtml()));

        doc = Jsoup.parse("<!DOCTYPE \u0000>");
        assertEquals(
                "<!DOCTYPE �> <html> <head></head> <body></body> </html>",
                StringUtil.normaliseWhitespace(doc.outerHtml()));
    }