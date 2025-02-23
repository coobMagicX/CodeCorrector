public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Ensure correct type is used
    long totalMillis = getMillis();

    // Add up all time parts down to millis
    totalMillis += getSeconds() * DateTimeConstants.MILLIS_PER_SECOND;
    totalMillis += getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE;
    totalMillis += getHours() * DateTimeConstants.MILLIS_PER_HOUR;
    totalMillis += getDays() * DateTimeConstants.MILLIS_PER_DAY;
    totalMillis += getWeeks() * DateTimeConstants.MILLIS_PER_WEEK;

    // Handle months and years separately as totalMillis does not include their conversion
    long totalMonths = (long) getMonths() + ((long) getYears()) * 12;
    totalMonths += totalMillis / DateTimeConstants.MILLIS_PER_MONTH;  // Convert leftover millis to months if applicable

    // Reset millis which were converted to months
    totalMillis %= DateTimeConstants.MILLIS_PER_MONTH;

    Period result = new Period(0, type, ISOChronology.getInstanceUTC()); // Start with zero millis

    // Only set fields that are supported by the PeriodType
    if (type.isSupported(DurationFieldType.months()) && totalMonths != 0) {
        result = result.withMonths((int) totalMonths);
    }

    if (type.isSupported(DurationFieldType.years())) {
        int years = (int) (totalMonths / 12);
        result = result.withYears(years);
        totalMonths %= 12;
        if (type.isSupported(DurationFieldType.months())) {
            result = result.withMonths((int) totalMonths);
        }
    }

    return result;
}
