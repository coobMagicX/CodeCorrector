    public void testMinusMonths_int_fromLeap() {
        MonthDay test = new MonthDay(2, 29, ISOChronology.getInstanceUTC());
        MonthDay result = test.minusMonths(1);
        MonthDay expected = new MonthDay(1, 29, ISOChronology.getInstance());
        assertEquals(expected, result);
    }