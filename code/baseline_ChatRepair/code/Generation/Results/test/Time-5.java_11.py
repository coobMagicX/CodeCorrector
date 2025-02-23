public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);  // Ensure the PeriodType is correct
    long millis = 0;

    // Handle smaller units up to hours
    if (type.isSupported(DurationFieldType.seconds()) || type.isSupported(DurationFieldType.millis())) {
        millis += getMillis();
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
    }
    if (type.isSupported(DurationFieldType.minutes())) {
        millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
    }
    if (type.isSupported(DurationFieldType.hours())) {
        millis += ((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR;
    }

    // For larger units, perform conversions
    int days = type.isSupported(DurationFieldType.days()) ? getDays() : 0;
    int weeks = getWeeks();
    if (type.isSupported(DurationFieldType.days())) {
        // Convert weeks to days if days are supported by the PeriodType
        days += weeks * 7;
    }

    int months = getMonths();
    int years = getYears();
    if (type.isSupported(DurationFieldType.months())) {
        // Aggregate years into months if only months are supported
        months += years * 12;
    } else if (type.isSupported(DurationFieldType.years())) {
        // Ensure both months and years are counted in terms of years if months are not individually supported
        years += months / 12;
        if (type.isSupported(DurationFieldType.months())) {
            months %= 12;
        } else {
            months = 0;  // Discard months if not supported
        }
    }

    // Create a Period from the millisecond total and adjust larger units here
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    if (type.isSupported(DurationFieldType.years())) {
        result = result.withYears(years);
    }
    if (type.isSupported(DurationFieldType.months())) {
        result = result.withMonths(months);
    }
    if (type.isSupported(DurationFieldType.days())) {
        result = result.withDays(days);
    }

    return result;
}
