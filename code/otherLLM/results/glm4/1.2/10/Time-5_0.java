public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis(); // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();

    // Calculate the number of extra years and remaining months
    if (years != 0 || months != 0) {
        // Use FieldUtils.safeAdd to handle potential overflow or underflow
        long extraYears = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;

        // Adjust the result period with calculated years and months
        if (extraYears != 0) {
            result = result.withYears(extraYears);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }

    return result;
}