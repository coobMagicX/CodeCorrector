public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    long instant = 0L;
    if (DateTimeUtils.isContiguous(partial)) {
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            Field<?> field = partial.getFieldType(i);
            int value = values[i];
            
            if (field == DateTimeField.MONTH_OF_YEAR) {
                // Adjust the instant when adding/subtracting months
                long newInstant = addWrapField(instant, valueToAdd);
                return iChronology.get(partial, newInstant);
            } else {
                instant = field.getField(iChronology).set(instant, value);
            }
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }

    // Default to returning original array if no adjustments were needed
    return values;
}

// Helper method to wrap around the year when adding months, considering leap years
private long addWrapField(long instant, int months) {
    boolean isLeapYear = (ChronoField.YEAR_OF_ERA.get(instant) % 4 == 0 && 
                          ChronoField.YEAR_OF_ERA.get(instant) % 100 != 0 || 
                          ChronoField.YEAR_OF_ERA.get(instant) % 400 == 0);
    boolean isFebruary29 = (ChronoField.MONTH_OF_YEAR.get(instant) == 2 && 
                            isLeapYear && 
                            ChronoField.DAY_OF_MONTH.get(instant) == 29);

    int newMonth = ChronoField.MONTH_OF_YEAR.get(instant) + months;
    
    // Handle the transition from February to March
    if (isFebruary29 && newMonth > 2) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), 1, MIN, iMax));
    } else {
        return set(instant, newMonth);
    }
}