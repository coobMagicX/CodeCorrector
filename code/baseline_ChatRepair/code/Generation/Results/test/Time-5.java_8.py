public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Validate and possibly adjust the PeriodType

    // Combine all time-related fields into milliseconds
    long millis = getMillis();
    millis += (((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND);
    millis += (((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE);
    millis += (((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR);
    millis += (((long) getDays()) * DateTimeConstants.MILLIS_PER_DAY);
    millis += (((long) getWeeks()) * DateTimeConstants.MILLIS_PER_WEEK);

    // Convert total years to months if necessary
    int totalMonths = getYears() * 12 + getMonths();

    // Initialize the result Period with the calculated milliseconds and specific type
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    
    // Handle years and months according to the support in the PeriodType
    if (type.isSupported(DurationFieldType.years())) {
        result = result.withYears(totalMonths / 12);  // Set years from total months
    }
    if (type.isSupported(DurationFieldType.months())) {
        result = result.withMonths(totalMonths % 12);  // Set the remainder of the months
    }

    return result;
}
