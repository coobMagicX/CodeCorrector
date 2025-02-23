public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Validates and possibly adjusts the type
    long millis = getMillis();  // combine smaller units into milliseconds
    millis += (((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND);
    millis += (((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE);
    millis += (((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR);
    millis += (((long) getDays()) * DateTimeConstants.MILLIS_PER_DAY);
    millis += (((long) getWeeks()) * DateTimeConstants.MILLIS_PER_WEEK);
    
    // Calculate total months from years and months fields
    int totalMonths = getYears() * 12 + getMonths();  // convert years to months and add to months
    
    // Create period with accumulated milliseconds in specified type and normalized months.
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    
    // If the period type supports months, then reset it with computed total months
    if (type.isSupported(DurationFieldType.months())) {
        result = result.withMonths(totalMonths);
    } else if (type.isSupported(DurationFieldType.years()) && totalMonths != 0) {
        // If months are not supported but years are, split totalMonths to set in years
        result = result.withYears(totalMonths / 12);
    }

    return result;
}
