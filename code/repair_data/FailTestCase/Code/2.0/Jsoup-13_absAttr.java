    @Test public void absAttr() {
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='http://jsoup.org'>Two</a>");
        Elements one = doc.select("#1");
        Elements two = doc.select("#2");
        Elements both = doc.select("a");

        assertEquals("", one.attr("abs:href"));
        assertEquals("http://jsoup.org", two.attr("abs:href"));
        assertEquals("http://jsoup.org", both.attr("abs:href"));
    }