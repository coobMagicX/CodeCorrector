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
    
    // Extract years and months from the period
    long[] values = result.getValues();  // cloned
    int yearValue = values[PeriodType.YEAR_INDEX];
    int monthValue = values[PeriodType.MONTH_INDEX];

    if (years != 0 || months != 0) {
        // Calculate years to add and remaining months after whole years are added
        years = FieldUtils.safeAdd(yearValue, months / 12);
        months = (int)(months % 12);

        // Apply the changes back to the period values
        if (years != 0) {
            getPeriodType().setIndexedField(this, PeriodType.YEAR_INDEX, values, years);
            result = new Period(values, type);
        }
        if (months != 0) {
            getPeriodType().setIndexedField(this, PeriodType.MONTH_INDEX, values, months);
            result = new Period(values, type);
        }
    }

    return result;
}