    public void testParseInto_monthDay_withDefaultYear_feb29() {
        DateTimeFormatter f = DateTimeFormat.forPattern("M d").withDefaultYear(2012);
        MutableDateTime result = new MutableDateTime(2004, 1, 9, 12, 20, 30, 0, LONDON);
        assertEquals(4, f.parseInto(result, "2 29", 0));
        assertEquals(new MutableDateTime(2004, 2, 29, 12, 20, 30, 0, LONDON), result);
    }