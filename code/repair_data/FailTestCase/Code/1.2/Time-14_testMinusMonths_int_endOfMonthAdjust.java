    public void testMinusMonths_int_endOfMonthAdjust() {
        MonthDay test = new MonthDay(3, 31, ISOChronology.getInstanceUTC());
        MonthDay result = test.minusMonths(1);
        MonthDay expected = new MonthDay(2, 29, ISOChronology.getInstance());
        assertEquals(expected, result);
    }