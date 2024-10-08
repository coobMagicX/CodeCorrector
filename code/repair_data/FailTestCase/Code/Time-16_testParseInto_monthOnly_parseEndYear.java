    public void testParseInto_monthOnly_parseEndYear() {
        DateTimeFormatter f = DateTimeFormat.forPattern("M").withLocale(Locale.UK);
        MutableDateTime result = new MutableDateTime(2004, 1, 31, 12, 20, 30, 0,TOKYO);
        assertEquals(2, f.parseInto(result, "12", 0));
        assertEquals(new MutableDateTime(2004, 12, 31, 12, 20, 30, 0, TOKYO), result);
    }