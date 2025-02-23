public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    Chronology chrono = ISOChronology.getInstanceUTC();
    long millis = getMillis();
    
    if (type.isSupported(DurationFieldType.seconds())) {
        millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    }
    if (type.isSupported(DurationFieldType.minutes())) {
        millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    }
    if (type.isSupported(DurationFieldType.hours())) {
        millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    }
    if (type.isSupported(DurationFieldType.days())) {
        millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    }
    if (type.isSupported(DurationFieldType.weeks())) {
        millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
    }
    
    Period result = new Period(millis, type, chrono);
    
    // Handle years and months if supported by the PeriodType
    int years = type.isSupported(DurationFieldType.years()) ? getYears() : 0;
    int months = type.isSupported(DurationFieldType.months()) ? getMonths() : 0;
    if (years != 0 || months != 0) {
        years = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;
        if (years != 0) {
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    
    return result;
}
