    @Test public void testGetSiblingsWithDuplicateContent() {
        Document doc = Jsoup.parse("<div><p>Hello<p id=1>there<p>this<p>this<p>is<p>an<p id=last>element</div>");
        Element p = doc.getElementById("1");
        assertEquals("there", p.text());
        assertEquals("Hello", p.previousElementSibling().text());
        assertEquals("this", p.nextElementSibling().text());
        assertEquals("this", p.nextElementSibling().nextElementSibling().text());
        assertEquals("is", p.nextElementSibling().nextElementSibling().nextElementSibling().text());
        assertEquals("Hello", p.firstElementSibling().text());
        assertEquals("element", p.lastElementSibling().text());
    }