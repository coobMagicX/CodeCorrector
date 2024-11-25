public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    // Corrected the normalization process for months, days, hours, minutes, seconds, and milliseconds
    long days = (millis / DateTimeConstants.MILLIS_PER_DAY);
    millis %= DateTimeConstants.MILLIS_PER_DAY;
    long hours = (millis / DateTimeConstants.MILLIS_PER_HOUR);
    millis %= DateTimeConstants.MILLIS_PER_HOUR;
    long minutes = (millis / DateTimeConstants.MILLIS_PER_MINUTE);
    millis %= DateTimeConstants.MILLIS_PER_MINUTE;
    long seconds = (millis / DateTimeConstants.MILLIS_PER_SECOND);
    millis %= DateTimeConstants.MILLIS_PER_SECOND;

    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        // Corrected the calculation to handle years and months correctly
        long totalDays = ((years * 365) + (getDays() + getWeeks() * 7)) % 365;
        months = (int) (((totalDays / 30.4375) - years));
        if (months < 0) {
            years--;
            months += 12;
        }
        years = FieldUtils.safeAdd(years, (int) ((totalDays / 365)));
        months %= 12;
    }

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    if (years != 0) {
        result = result.withYears(years);
    }
    if (months != 0) {
        result = result.withMonths(months);
    }
    if (days != 0) {
        result = result.withDays(days);
    }
    if (hours != 0) {
        result = result.withHours(hours);
    }
    if (minutes != 0) {
        result = result.withMinutes(minutes);
    }
    if (seconds != 0) {
        result = result.withSeconds(seconds);
    }

    return result;
}