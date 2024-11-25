public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();
    // overflow handling for seconds, minutes, hours, days, weeks to milliseconds
    if (getSeconds() > 0 || getMinutes() > 0 || getHours() > 0 || getDays() > 0 || getWeeks() > 0) {
        millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
        millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
        millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
        millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
        millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
    }
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        // accurate calculation for years and months
        long totalDays = (long) years * DateTimeConstants.DAYS_PER_YEAR + (long) months * DateTimeConstants.DAYS_PER_MONTH;
        millis += totalDays * ((long) DateTimeConstants.MILLIS_PER_DAY);
        result = new Period(millis, type, ISOChronology.getInstanceUTC());
        if (years != 0) {
            // verify period normalization
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    return result;
}