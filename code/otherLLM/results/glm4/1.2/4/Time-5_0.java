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

    // Check if the period is solely in terms of years or months
    checkYearsAndMonths(type.toString());

    if (years != 0 || months != 0) {
        if (type == PeriodType.YEARS || type == PeriodType.MONTHS) {
            // If only months are present, normalize the year value accordingly
            if (months != 0 && type == PeriodType.MONTHS) {
                years = FieldUtils.safeAdd(years, months / 12);
                months = months % 12;
            } else {
                // Normalize month value within a year
                months += years * 12;
                years = 0;
            }
        }

        if (years != 0) {
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    
    return result;
}