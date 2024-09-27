    public void testParseLocalDate_year_month_week_2010() {
        Chronology chrono = GJChronology.getInstanceUTC();
        DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-ww").withChronology(chrono);
        assertEquals(new LocalDate(2010, 1, 4, chrono), f.parseLocalDate("2010-01-01"));
    }