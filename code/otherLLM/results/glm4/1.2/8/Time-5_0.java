public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    int years = getYears();
    int months = getMonths();

    // Check if the period is valid for normalization
    checkYearsAndMonths("standard");

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());

    if (months != 0) {
        // If months are non-zero, add them to the period as full months
        result = result.withMonths(months);
    } else if (years != 0) {
        // If years are non-zero and months are zero, calculate the total number of months in those years
        int totalMonthsInYears = years * 12;
        result = result.withMonths(totalMonthsInYears);
    }
    
    return result;
}