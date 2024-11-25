public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis(); // no overflow can happen, even with Integer.MAX_VALUEs
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();

    // Ensure the period does not have a negative sign
    if (years < 0 || months < 0) {
        throw new IllegalArgumentException("Period cannot be negative");
    }

    // Normalize years and months before adding to the result
    if (years != 0 || months != 0) {
        Period tempPeriod = result;
        if (months >= DateTimeConstants.MILLIS_PER_MONTH) {
            int extraYears = months / DateTimeConstants.MILLIS_PER_MONTH;
            int remainingMonths = months % DateTimeConstants.MILLIS_PER_MONTH;

            // Add years and remaining months to the period
            tempPeriod = tempPeriod.withYears(tempPeriod.getYears() + extraYears)
                                    .withMonths(remainingMonths);

            // Reset months to 0 if there were no remaining months after adding years
            if (remainingMonths == 0) {
                months = 0;
            }
        }

        // Add any remaining years or months to the result
        if (years != 0 || months != 0) {
            if (years != 0) {
                tempPeriod = tempPeriod.withYears(tempPeriod.getYears() + years);
            }
            if (months != 0) {
                tempPeriod = tempPeriod.withMonths(months);
            }
        }

        result = tempPeriod;
    }

    return result;
}