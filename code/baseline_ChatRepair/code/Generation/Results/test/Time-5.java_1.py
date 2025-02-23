public Period normalizedStandard(PeriodType type) {
    // Ensure we are dealing with the correct type
    type = DateTimeUtils.getPeriodType(type);
    Chronology chrono = ISOChronology.getInstanceUTC();
    
    // Initialize result with 0 millis and the required type
    Period result = new Period(0L, type, chrono);
    
    // Process years and months only if they are supported
    if (type.isSupported(DurationFieldType.years()) || type.isSupported(DurationFieldType.months())) {
        int years = getYears();
        int months = getMonths();

        // Normalize months into years and months (handle overflow)
        years = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;
        
        // Assign normalized years and months back to result if they are supported
        if (type.isSupported(DurationFieldType.years()) && years != 0) {
            result = result.withYears(years);
        }
        if (type.isSupported(DurationFieldType.months()) && months != 0) {
            result = result.withMonths(months);
        }
    }
    
    return result;
}
