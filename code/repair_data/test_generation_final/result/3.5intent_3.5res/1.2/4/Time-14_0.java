public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }

    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);

        // Check if the resulting date is February 29 of a leap year
        if (partial.getFieldType(fieldIndex).getField(iChronology).getMaximumValue() == 29 && isLeapYear(partial.getYear())) {
            // Adjust the resulting date to the last valid day of February in non-leap years
            if (partial.getMonthOfYear() == 2 && partial.getDayOfMonth() == 29 && !isLeapYear(partial.getYear() + valueToAdd)) {
                instant = iChronology.dayOfMonth().set(instant, 28);
            }
        }

        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

public boolean isLeapYear(int year) {
    return iChronology.year().isLeap(year);
}