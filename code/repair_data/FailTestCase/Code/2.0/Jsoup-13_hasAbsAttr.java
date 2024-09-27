    @Test public void hasAbsAttr() {
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='http://jsoup.org'>Two</a>");
        Elements one = doc.select("#1");
        Elements two = doc.select("#2");
        Elements both = doc.select("a");
        assertFalse(one.hasAttr("abs:href"));
        assertTrue(two.hasAttr("abs:href"));
        assertTrue(both.hasAttr("abs:href")); // hits on #2
    }