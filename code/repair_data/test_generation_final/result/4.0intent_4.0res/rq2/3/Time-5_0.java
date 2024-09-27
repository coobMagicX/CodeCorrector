public class Period {
    private int years;
    private int months;
    private int weeks;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int millis;

    public Period(int years, int months, int weeks, int days, int hours, int minutes, int seconds, int millis) {
        this.years = years;
        this.months = months;
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
    }

    public long getMillis() { return millis; }
    public int getSeconds() { return seconds; }
    public int getMinutes() { return minutes; }
    public int getHours() { return hours; }
    public int getDays() { return days; }
    public int getWeeks() { return weeks; }
    public int getYears() { return years; }
    public int getMonths() { return months; }

    public Period normalizedStandard(PeriodType type) {
        type = DateTimeUtils.getPeriodType(type);
        long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
        millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
        millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
        millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
        millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
        millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
        Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
        int years = getYears();
        int months = getMonths();
        if (years != 0 || months != 0) {
            years = FieldUtils.safeAdd(years, months / 12);
            months = months % 12;
            if (years != 0) {
                result = result.withYears(years);
            }
            if (months != 0) {
                result = result.withMonths(months);
            }
        }
        return result;
    }

    public void normalizedStandardTest() {
        Period test = new Period(1, 15, 0, 0, 0, 0, 0, 0);
        Period result = test.normalizedStandard(PeriodType.months());
        assertEquals(new Period(2, 3, 0, 0, 0, 0, 0, 0), result);
    }

    private void assertEquals(Period expected, Period actual) {
        assert expected.getYears() == actual.getYears() &&
               expected.getMonths() == actual.getMonths() &&
               expected.getWeeks() == actual.getWeeks() &&
               expected.getDays() == actual.getDays() &&
               expected.getHours() == actual.getHours() &&
               expected.getMinutes() == actual.getMinutes() &&
               expected.getSeconds() == actual.getSeconds() &&
               expected.getMillis() == actual.getMillis();
    }

    // Additional methods like withYears, withMonths, DateTimeUtils.getPeriodType, DateTimeConstants, FieldUtils.safeAdd
    // would be defined similarly to complete this example.
}