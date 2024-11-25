public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis(); // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    int years = getYears();
    int months = getMonths();

    // First, handle the case where there are no years or months
    if (years == 0 && months == 0) {
        Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
        return result;
    }

    // If there are years, add them to the total count of months
    int totalMonths = FieldUtils.safeAdd(months, years * 12);

    // Now set the years and months correctly by using withMonths method
    Period result = new Period(totalMonths, type, ISOChronology.getInstanceUTC());
    if (months % 12 != 0) {
        result = result.withMonths((int)(totalMonths % 12));
    }
    
    return result;
}