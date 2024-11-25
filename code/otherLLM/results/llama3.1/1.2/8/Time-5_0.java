public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        int totalMonths = FieldUtils.safeAdd(years * 12, months); // corrected month handling
        int remainingDays = getDays(); // added day handling
        result = new Period(totalMonths, type, ISOChronology.getInstanceUTC()); // updated period creation
        if (remainingDays > 0) {
            result = result.withDays(remainingDays);
        }
    }
    return result;
}