    public void testPlusMonths_int_negativeEndOfMonthAdjust() {
        MonthDay test = new MonthDay(3, 31, ISOChronology.getInstanceUTC());
        MonthDay result = test.plusMonths(-1);
        MonthDay expected = new MonthDay(2, 29, ISOChronology.getInstance());
        assertEquals(expected, result);
    }