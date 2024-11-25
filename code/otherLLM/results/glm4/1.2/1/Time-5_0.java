public Period normalizedStandard(PeriodType type) {
    checkYearsAndMonths("years and months");
    
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
    
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    
    // Normalize the period to an equivalent period of the specified type
    if (type == PeriodType.years()) {
        years += months / 12;
        months %= 12;
    } else if (type == PeriodType.months()) {
        months += years * 12;
        years = 0; // Reset years as they are not part of the months period type
    }
    
    if (years != 0) {
        result = result.withYears(years);
    }
    if (months != 0 && type == PeriodType.months()) {
        result = result.withMonths(months);
    }
    
    return result;
}