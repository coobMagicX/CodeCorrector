public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    // Correctly handle months period type
    int months = getMonths();
    if (type == PeriodType.MONTHS) {
        millis = months * 2419200L; // 2419200 milliseconds in a month
    }

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    if (years != 0) {
        result = result.withYears(years);
    }
    return result;
}