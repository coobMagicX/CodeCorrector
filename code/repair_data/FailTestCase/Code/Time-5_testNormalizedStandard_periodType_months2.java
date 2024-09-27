    public void testNormalizedStandard_periodType_months2() {
        Period test = new Period(-2, 15, 0, 0, 0, 0, 0, 0);
        Period result = test.normalizedStandard(PeriodType.months());
        assertEquals(new Period(-2, 15, 0, 0, 0, 0, 0, 0), test);
        assertEquals(new Period(0, -9, 0, 0, 0, 0, 0, 0, PeriodType.months()), result);
    }