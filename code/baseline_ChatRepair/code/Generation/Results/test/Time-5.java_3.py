public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Ensure the correct PeriodType is used
    long millis = getMillis();
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    // Create a period with only time-based fields from the millis and original type
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());

    // Get years and months separately
    int years = getYears();
    int months = getMonths();

    // Adjust the years and months if necessary
    if (years != 0 || months != 0) {
        years = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;
        // Only set years and months if they are supported by the type
        if (type.isSupported(DurationFieldType.years())) {
            if (years != 0) {
                result = result.withYears(years);
            }
        }
        if (type.isSupported(DurationFieldType.months())) {
            if (months != 0) {
                result = result.withMonths(months);
            }
        }
    }
    return result;
}
