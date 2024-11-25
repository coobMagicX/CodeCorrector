public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();

    // Calculate the total number of months
    int totalMonths = years * 12 + months;

    // Account for leap days when converting between year/month representations
    if (type == PeriodType.months) {
        long millisPerMonth = DateTimeConstants.MILLIS_PER_MONTH;
        totalMonths += (millis / millisPerMonth);
    }

    // Correctly handle periods with a large number of months or years
    int finalYears = FieldUtils.safeDiv(totalMonths, 12);
    int finalMonths = FieldUtils.safeMod(totalMonths, 12);

    if (finalYears != 0 || finalMonths != 0) {
        result = result.withYears(finalYears);
        if (finalMonths != 0 && type == PeriodType.months) {
            result = result.withMonths(finalMonths);
        } else {
            int remainingDays = FieldUtils.safeMod((int) ((millis / DateTimeConstants.MILLIS_PER_DAY)), 30);
            result = result.withDays(remainingDays);
        }
    }

    return result;
}