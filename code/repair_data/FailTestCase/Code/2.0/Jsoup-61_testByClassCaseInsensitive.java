    @Test public void testByClassCaseInsensitive() {
        String html = "<p Class=foo>One <p Class=Foo>Two <p class=FOO>Three <p class=farp>Four";
        Elements elsFromClass = Jsoup.parse(html).select("P.Foo");
        Elements elsFromAttr = Jsoup.parse(html).select("p[class=foo]");

        assertEquals(elsFromAttr.size(), elsFromClass.size());
        assertEquals(3, elsFromClass.size());
        assertEquals("Two", elsFromClass.get(1).text());
    }