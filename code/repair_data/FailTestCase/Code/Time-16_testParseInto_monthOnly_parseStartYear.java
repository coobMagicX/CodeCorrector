    public void testParseInto_monthOnly_parseStartYear() {
        DateTimeFormatter f = DateTimeFormat.forPattern("M").withLocale(Locale.UK);
        MutableDateTime result = new MutableDateTime(2004, 2, 1, 12, 20, 30, 0, TOKYO);
        assertEquals(1, f.parseInto(result, "1", 0));
        assertEquals(new MutableDateTime(2004, 1, 1, 12, 20, 30, 0, TOKYO), result);
    }