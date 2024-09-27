    public void testMinusDays_int_negativeFromLeap() {
        MonthDay test = new MonthDay(2, 29, ISOChronology.getInstanceUTC());
        MonthDay result = test.minusDays(-1);
        MonthDay expected = new MonthDay(3, 1, ISOChronology.getInstance());
        assertEquals(expected, result);
    }