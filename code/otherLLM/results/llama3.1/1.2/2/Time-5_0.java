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
    
    // Handle periods with months as the primary unit
    if (type == PeriodType.months()) {
        long monthMillis = (long) DateTimeConstants.MILLIS_PER_MONTH;
        millis += (months * monthMillis);
        
        // Update result with new total millis
        result = new Period(millis, type, ISOChronology.getInstanceUTC());
    } else {
        if (years != 0 || months != 0) {
            years = FieldUtils.safeAdd(years, months / 12);
            months = months % 12;
            
            // Update result with calculated years and months
            result = result.withYears(years).withMonths(months);
        }
    }
    
    return result;
}