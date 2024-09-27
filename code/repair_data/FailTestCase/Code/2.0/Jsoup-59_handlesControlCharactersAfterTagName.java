    @Test public void handlesControlCharactersAfterTagName() {
        String html = "<a/\06>";
        String clean = Jsoup.clean(html, Whitelist.basic());
        assertEquals("<a rel=\"nofollow\"></a>", clean);
    }