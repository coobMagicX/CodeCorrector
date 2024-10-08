    public void testParseInto_monthOnly() {
        DateTimeFormatter f = DateTimeFormat.forPattern("M").withLocale(Locale.UK);
        MutableDateTime result = new MutableDateTime(2004, 1, 9, 12, 20, 30, 0, LONDON);
        assertEquals(1, f.parseInto(result, "5", 0));
        assertEquals(new MutableDateTime(2004, 5, 9, 12, 20, 30, 0, LONDON), result);
    }