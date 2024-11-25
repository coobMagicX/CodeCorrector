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
    // Roll over the months to years and calculate the remainder in months
    if (months >= 12) {
        years += months / 12;
        months %= 12;
    }
    
    // Add the rolled over years and months to the result period
    result = result.withYears(years);
    result = result.withMonths(months);

    return result;
}