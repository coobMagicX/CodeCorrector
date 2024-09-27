    public void testNormalizedStandard_periodType_monthsWeeks() {
        PeriodType type = PeriodType.forFields(new DurationFieldType[]{
                        DurationFieldType.months(),
                        DurationFieldType.weeks(),
                        DurationFieldType.days()});
        Period test = new Period(2, 4, 6, 0, 0, 0, 0, 0);
        Period result = test.normalizedStandard(type);
        assertEquals(new Period(2, 4, 6, 0, 0, 0, 0, 0), test);
        assertEquals(new Period(0, 28, 6, 0, 0, 0, 0, 0, type), result);
    }