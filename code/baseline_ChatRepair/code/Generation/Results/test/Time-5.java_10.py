public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Ensure the PeriodType is correct
    long millis = 0;

    if (type.isSupported(DurationFieldType.millis())) {
        millis += getMillis();
    }
    if (type.isSupported(DurationFieldType.seconds())) {
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
    }
    if (type.isSupported(DurationFieldType.minutes())) {
        millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
    }
    if (type.isSupported(DurationFieldType.hours())) {
        millis += ((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR;
    }
    if (type.isSupported(DurationFieldType.days())) {
        millis += ((long) getDays()) * DateTimeConstants.MILLIS_PER_DAY;
    }
    if (type.isSupported(DurationFieldType.weeks())) {
        millis += ((long) getWeeks()) * DateTimeConstants.MILLIS_PER_WEEK;
    }

    // Create a Period for the calculated millisecond duration
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());

    // Adjust years and months based on what the PeriodType supports
    int totalMonths = getMonths();
    if (type.isSupported(DurationFieldType.months())) {
        totalMonths += getYears() * 12;  // Convert years to months if months are supported
        result = result.withMonths(totalMonths);
    } else if (type.isSupported(DurationFieldType.years())) {
        int years = getYears() + totalMonths / 12;  // Convert months to years if years are supported
        int remainingMonths = totalMonths % 12;
        result = result.withYears(years);
        if (type.isSupported(DurationFieldType.months())) {
            result = result.withMonths(remainingMonths);
        }
    }

    return result;
}
