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
        int[] newValues = iChronology.get(partial, instant);
        // Check if day needs adjustment (e.g., February 29 in a non-leap year)
        if (!iChronology.isValid(partial, newValues)) {
            // Adjust to the last valid day of the month if the day is invalid
            int maxDayOfMonth = iChronology.getDaysInMonthMax(newValues[1], newValues[0]);
            newValues[2] = maxDayOfMonth;
        }
        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}