    @Test public void handlesKnownEmptyNoFrames() {
        String h = "<html><head><noframes /><meta name=foo></head><body>One</body></html>";
        Document doc = Jsoup.parse(h);
        assertEquals("<html><head><noframes></noframes><meta name=\"foo\"></head><body>One</body></html>", TextUtil.stripNewlines(doc.html()));
    }