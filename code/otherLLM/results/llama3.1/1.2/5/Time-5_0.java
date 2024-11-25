public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    long weeks = getWeeks();
    millis += (weeks * ((long) DateTimeConstants.MILLIS_PER_WEEK));
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    
    // Reconcile year/months adjustment
    if (years != 0 || months != 0) {
        // Adjust calculation of weeks to consider month/week conversion
        long totalWeeks = (years * 52L) + months;
        weeks += FieldUtils.safeAdd(weeks, (int)(totalWeeks % 52));
        int remainingMonths = (int)((totalWeeks / 52) * 12);
        
        years = FieldUtils.safeAdd(years, remainingMonths / 12);
        months = remainingMonths % 12;
        
        if (years != 0) {
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    
    return result;
}