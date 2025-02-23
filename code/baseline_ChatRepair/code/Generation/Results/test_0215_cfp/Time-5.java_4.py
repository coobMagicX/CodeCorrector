public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long totalMillis = getMillis();
    totalMillis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    totalMillis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    totalMillis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    totalMillis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    totalMillis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    // Convert all millis into the appropriate units if supported
    int months = 0;
    if (type.isSupported(DurationFieldType.months())) {
        long totalDays = totalMillis / DateTimeConstants.MILLIS_PER_DAY;
        months += totalDays / 30;  // Simple conversion, 1 month = 30 days
    }

    // Add existing months and years, assuming years to be converted directly into months
    months += getMonths();
    months += getYears() * 12;

    // Use derived months and create a new period from it within the specified type
    Period result = new Period(type, ISOChronology.getInstanceUTC());
    result = result.withMonths(months); // Should check if months are supported but is implied here
    return result;
}
