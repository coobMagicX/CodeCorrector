public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);

    // Calculate the total time in milliseconds starting with the smallest unit
    long millis = getMillis();
    millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
    millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
    millis += ((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR;
    millis += ((long) getDays()) * DateTimeConstants.MILLIS_PER_DAY;
    millis += ((long) getWeeks()) * DateTimeConstants.MILLIS_PER_WEEK;

    // Create a new period with a type that only includes supported fields(using base constructor)
    Period result = new Period(0, type, ISOChronology.getInstanceUTC());

    // If months type is supported, decompose all time components into months
    if (type.isSupported(DurationFieldType.months())) {
        int totalMonths = getMonths();
        totalMonths += getYears() * 12;

        // Converting smaller units using rough average conversions
        totalMonths += ((millis / DateTimeConstants.MILLIS_PER_DAY) / 30); // Approx days to month

        result = result.withMonths(totalMonths);
    }

    return result;
}
