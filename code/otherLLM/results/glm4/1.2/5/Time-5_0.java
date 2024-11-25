public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis(); // no overflow can happen, even with Integer.MAX_VALUEs

    // Add milliseconds for seconds, minutes, hours, days, and weeks.
    millis += ((long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND);
    millis += ((long) getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE);
    millis += ((long) getHours() * DateTimeConstants.MILLIS_PER_HOUR);
    millis += ((long) getDays() * DateTimeConstants.MILLIS_PER_DAY);
    millis += ((long) getWeeks() * DateTimeConstants.MILLIS_PER_WEEK);

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();

    // Adjust for full years and months.
    if (years != 0 || months != 0) {
        long totalYearsToAdd = FieldUtils.safeAdd(years, months / 12);
        long totalMonthsToAdd = months % 12;

        // Add years to the result
        if (totalYearsToAdd != 0) {
            result = result.withYears(totalYearsToAdd);
        }
        // Add months to the result
        if (totalMonthsToAdd != 0) {
            result = result.withMonths(totalMonthsToAdd);
        }
    }

    return result;
}